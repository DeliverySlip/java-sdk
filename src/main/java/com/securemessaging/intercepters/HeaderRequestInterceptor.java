package com.securemessaging.intercepters;


import com.securemessaging.utils.BuildVersion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;

/**
 * HeaderRequestInterceptor is an interceptor for all requests so as to add the appropriate headers needed to
 * authenticate with the server.
 */
public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {

    private String token;

    private boolean tokenSet = false;

    private boolean skipHeaders = false;

    private String clientName = "secure-messenger-java";
    private String clientVersion = BuildVersion.getBuildVersion();

    /**
     * addSessionToken adds the sessionToken to the header interceptor so that it can be added with all other requests
     * to the api for authentication
     * @param token String the authentication token returned in the Session object after logging in
     */
    public void addSessionToken(String token){
        this.token = token;
        tokenSet = true;
    }

    public void setClientName(String clientName){
        this.clientName = clientName;
    }

    public void setClientVersion(String clientVersion){
        this.clientVersion = clientVersion;
    }

    public String getClientVersion(){return this.clientVersion;}

    // - deprecated - used for uploading attachments with the Apache Commons HTTP library
    public String getSessionToken(){
        return this.token;
    }

    //when uploading attachments - the attachment uses form data and thus sets its own headers
    public void skipContentTypeAndAcceptHeadersForNextCall(){
        skipHeaders = true;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpRequest wrapper = new HttpRequestWrapper(request);

        //System.out.println("Body of request");
        //System.out.println(new String(body));
        //meta headers
        wrapper.getHeaders().add("x-sm-client-name", this.clientName);
        wrapper.getHeaders().add("x-sm-client-version", this.clientVersion); //optional

        //accepted type and content-type
        if(!skipHeaders){
            wrapper.getHeaders().set(HttpHeaders.ACCEPT, "application/json");
            wrapper.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");
        }else{
            skipHeaders = false;
        }


        //if the token has been set we can add the token header then
        if(tokenSet){
            wrapper.getHeaders().add("x-sm-session-token", this.token);
        }

        //logging
        //System.out.println("HEADERS: " + wrapper.getHeaders().toString());
        //System.out.println("URI: " + wrapper.getURI().toString());
        //System.out.println("REQUEST BODY: " + new String(body));

        return execution.execute(wrapper, body);
    }

}
