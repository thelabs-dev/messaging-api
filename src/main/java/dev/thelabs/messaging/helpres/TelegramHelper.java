package dev.thelabs.messaging.helpres;

import dev.thelabs.messaging.ResponseApi;
import dev.thelabs.messaging.http.Proxy;
import dev.thelabs.messaging.http.Rest;

public class TelegramHelper {
    
	public static ResponseApi sendMessage(boolean debug, Proxy proxy, String token, String chat_id, String message){
        if (token.isEmpty() || chat_id.isEmpty())
            return new ResponseApi(false, 1, "El token o el canal no puede ser vacío");
        
        message = message.replace("\\n", "\n");
        String url = String.format("https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",token,Rest.encode(chat_id), Rest.encode(message));
        if (debug) System.out.println(url);
        ResponseApi response = Rest.Get(proxy, url);
        if (response.success){
            if (debug) System.out.println(response.data);
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