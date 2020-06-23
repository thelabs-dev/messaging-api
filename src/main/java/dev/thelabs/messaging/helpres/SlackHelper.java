package dev.thelabs.messaging.helpres;

import dev.thelabs.messaging.ResponseApi;
import dev.thelabs.messaging.http.Proxy;
import dev.thelabs.messaging.http.Rest;

public class SlackHelper{

	public static ResponseApi sendMessage(boolean debug, Proxy proxy, String token, String channel, String message, int secondsDelay, String botname, String iconurl){
        if (secondsDelay>0){
            return scheduleMessage(debug,proxy,token,channel,message,secondsDelay, botname, iconurl);
        }
        if (token.isEmpty() || channel.isEmpty())
            return new ResponseApi(false, 1, "El token o el canal no puede ser vacío");
            
        String url = "https://slack.com/api/chat.postMessage";
        message = message.replace("\\n", "\n");
        String data = "token="+ Rest.encode(token)+"&channel=" + Rest.encode(channel) +"&icon_url=" + Rest.encode(iconurl) + "&username=" + Rest.encode(botname) + "&text=" + Rest.encode(message);
        ResponseApi response = Rest.Post(proxy, url, "application/x-www-form-urlencoded",data);
        if (response.success){
            if (response.data.contains("\"ok\":false")){
                response.success = false;
                response.error_code = 3;
                response.error_message = response.data;
                response.data = "";
            }
        }
        return response;
    }

    private static ResponseApi scheduleMessage(Boolean debug, Proxy proxy, String token, String channel, String message, int secondsDelay, String botname, String iconurl){
        if (token.isEmpty() || channel.isEmpty())
            return new ResponseApi(false, 1, "El token o el canal no puede ser vacío");
            
        String url = "https://slack.com/api/chat.scheduleMessage";
        message = message.replace("\\n", "\n");
        String post_at = String.valueOf((System.currentTimeMillis() + (secondsDelay*1000L))/1000L);
        String data = "token="+ Rest.encode(token)+"&channel=" + Rest.encode(channel) +"&post_at=" + Rest.encode(post_at) +"&icon_url=" + Rest.encode(iconurl) + "&username=" + Rest.encode(botname) + "&text=" + Rest.encode(message);
        ResponseApi response = Rest.Post(proxy, url, "application/x-www-form-urlencoded",data);
        if (response.success){
            if (response.data.contains("\"ok\":false")){
                response.success = false;
                response.error_code = 3;
                response.error_message = response.data;
                response.data = "";
            }
        }
        return response;
    }
}
