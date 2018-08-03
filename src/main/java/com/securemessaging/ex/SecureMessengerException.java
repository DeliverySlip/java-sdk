package com.securemessaging.ex;

import com.securemessaging.sm.enums.SMRequestMethod;
import com.securemessaging.sm.response.meta.ResponseStatus;
import org.springframework.http.HttpMethod;

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

    private SMRequestMethod requestMethod = null;
    private String requestUrl = null;

    public SecureMessengerException(ResponseStatus responseStatus, int httpStatusCode, String rawResponseBody, SMRequestMethod requestMethod, String requestUrl){
        super("Secure Messaging API Exception:" +
                "\n\t Http Status Code: " + httpStatusCode +
                "\n\t ResponseBody: >" + rawResponseBody + "<" +
                "\n\t Request Method: " + requestMethod.name() +
                "\n\t Request URL: " + requestUrl);

        this.responseStatus = responseStatus;
        this.httpStatusCode = httpStatusCode;
        this.rawResponseBody = rawResponseBody;
        this.requestMethod = requestMethod;
        this.requestUrl = requestUrl;
    }

    public SMRequestMethod getRequestMethod(){
        return this.requestMethod;
    }

    public String getRequestUrl(){
        return this.requestUrl;
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
