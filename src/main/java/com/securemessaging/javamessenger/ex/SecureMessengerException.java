package com.securemessaging.javamessenger.ex;

import com.securemessaging.javamessenger.sm.response.meta.ResponseStatus;

public class SecureMessengerException extends Exception {

    private ResponseStatus responseStatus;
    private int httpStatusCode;
    private String rawResponseBody;

    public SecureMessengerException(ResponseStatus responseStatus, int httpStatusCode, String rawResponseBody){
        super("HTTP STATUS: " + httpStatusCode + ". ResponseBody: >" + rawResponseBody + "<");
        this.responseStatus = responseStatus;
        this.httpStatusCode = httpStatusCode;
        this.rawResponseBody = rawResponseBody;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public String getRawResponseBody(){
        return this.rawResponseBody;
    }

    public int getHttpStatusCode(){
        return this.httpStatusCode;
    }
}
