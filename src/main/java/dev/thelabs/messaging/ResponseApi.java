package dev.thelabs.messaging;

public class ResponseApi {
    public boolean success;
    public int error_code;
    public String error_message;
    public String data;

    public ResponseApi(boolean success, int error_code, String error_message){
        this.success = success;
        this.error_code = error_code;
        this.error_message = error_message;
    }

    public ResponseApi(boolean success, int error_code, String error_message, String data){
        this.success = success;
        this.error_code = error_code;
        this.error_message = error_message;
        this.data = data;
    }
}