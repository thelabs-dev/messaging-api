package dev.thelabs.messaging;

import dev.thelabs.messaging.http.Proxy;
import dev.thelabs.messaging.http.Rest;

public class SlackHelper{

	static ResponseApi sendMessage(Proxy proxy, String token, String channel, String message){
        if (token.isEmpty() || channel.isEmpty())
            return new ResponseApi(false, 1, "El token o el canal no puede ser vacío");
            
        String url = "https://slack.com/api/chat.postMessage";
	message = message.replace("\\n", "\n");
        String data = "token="+ Rest.encode(token)+"&channel=" + Rest.encode(channel) +"&text=" + Rest.encode(message);
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
