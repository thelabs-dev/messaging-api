package dev.thelabs.messaging.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Objects;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import dev.thelabs.messaging.MessagingAPI;
import dev.thelabs.messaging.ResponseApi;

public class Rest{
   
    public static ResponseApi Post(Proxy proxy, String url, String contentType, String data){
        CloseableHttpClient client = null;
        if (proxy != null){
            HttpClientBuilder clientHelper = HttpClientBuilder.create();
            HttpHost proxyHost = new HttpHost(proxy.hostname,proxy.port);
            if (proxyHost != null) {
                clientHelper.setProxy(proxyHost);
            }

            RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

            CredentialsProvider creds = new BasicCredentialsProvider();
            creds.setCredentials(AuthScope.ANY, new NTCredentials(proxy.username, proxy.password, getWorkstation(), proxy.userdomain));
            clientHelper.setDefaultCredentialsProvider(creds);
            client = clientHelper.setDefaultRequestConfig(globalConfig).build();
        }else{
            client = HttpClients.createDefault();
        }
        
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new StringEntity(data, ContentType.create(contentType, Consts.UTF_8)));
            CloseableHttpResponse response = null;

            response = client.execute(httpPost);
            
            StatusLine sl = response.getStatusLine();
            Boolean error = false;
            if (!(sl.getStatusCode() >= 200 && sl.getStatusCode() < 300)){
                //ERROR
                error = true;
            }
            String result = EntityUtils.toString(response.getEntity());
            
            client.close();
            return new ResponseApi(!error, (error? 2 : 0), (error? result : ""), (error? "" : result));
        } catch (IOException e) {
            if (MessagingAPI.debug)
                e.printStackTrace();
            return new ResponseApi(false, 3,  e.getMessage());
        }
        
    }

    public static ResponseApi Get(Proxy proxy, String url){
        CloseableHttpClient client = null;
        if (proxy != null){
            HttpClientBuilder clientHelper = HttpClientBuilder.create();
            HttpHost proxyHost = new HttpHost(proxy.hostname,proxy.port);
            if (proxyHost != null) {
                clientHelper.setProxy(proxyHost);
            }

            RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

            CredentialsProvider creds = new BasicCredentialsProvider();
            creds.setCredentials(AuthScope.ANY, new NTCredentials(proxy.username, proxy.password, getWorkstation(), proxy.userdomain));
            clientHelper.setDefaultCredentialsProvider(creds);
            client = clientHelper.setDefaultRequestConfig(globalConfig).build();
        }else{
            client = HttpClients.createDefault();
        }
        
        HttpGet httpGet = new HttpGet(url);

        try {
            CloseableHttpResponse response = null;

            response = client.execute(httpGet);
            
            StatusLine sl = response.getStatusLine();
            Boolean error = false;
            if (!(sl.getStatusCode() >= 200 && sl.getStatusCode() < 300)){
                //ERROR
                error = true;
            }
            String result = EntityUtils.toString(response.getEntity());
            
            client.close();
            return new ResponseApi(!error, (error? 2 : 0), (error? result : ""), (error? "" : result));
        } catch (IOException e) {
            if (MessagingAPI.debug)
                e.printStackTrace();
            return new ResponseApi(false, 3,  e.getMessage());
        }
        
    }

	private static String getWorkstation() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            return "Unknown";
        }
    }

    public static String encode(String param) {
        Objects.requireNonNull(param);
        String result = "";
        try {
            result = URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("encode charset error" + e.getMessage());
        }
        return result;
    }
}