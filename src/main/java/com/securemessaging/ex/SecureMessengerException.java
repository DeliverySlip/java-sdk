package com.securemessaging.ex;

import com.securemessaging.sm.response.meta.ResponseStatus;

/**
 * SecureMessengerException represents errors returned by the Messaging API. This exception is meant to give the
 * user the most insight into the cause of the error and parse that information from the Messaging APIs response
 * body. Clients should be able to report the information parsed into this exception to support as sufficient
 * information for support to investigate the issue further
 */
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
