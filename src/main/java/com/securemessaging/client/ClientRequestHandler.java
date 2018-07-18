package com.securemessaging.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.intercepters.HeaderRequestInterceptor;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.intercepters.ProxyRequestInterceptor;
import com.securemessaging.sm.Session;
import com.securemessaging.sm.response.meta.ResponseStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientRequestHandler is a wrapper for the Spring RestTemplate to make it easier and more portable to use and configure
 * around the Secure Messaging Client
 */
public class ClientRequestHandler {

    private RestTemplate restTemplate;
    private HeaderRequestInterceptor authInterceptor;
    private static ProxyRequestInterceptor proxyInterceptor;

    private String baseURL;

    private Session clientSession;

    public ClientRequestHandler(String baseURL){
        this.baseURL = baseURL;

        restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        authInterceptor = new HeaderRequestInterceptor();
        interceptors.add(authInterceptor);

        proxyInterceptor = new ProxyRequestInterceptor();
        interceptors.add(proxyInterceptor);

        restTemplate.setInterceptors(interceptors);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new ByteArrayHttpMessageConverter());
        // Add the message converters to the restTemplate
        restTemplate.setMessageConverters(messageConverters);


        if(onProxyInterceptionEventListenerInterface == null) {
            proxyInterceptor.setOnRequestInterceptionEventHandler(null);
        }else{
            proxyInterceptor.setOnRequestInterceptionEventHandler(new ProxyRequestInterceptor.OnRequestInterceptionEventHandler() {
                @Override
                public URI getProxyPath(URI originalUri) {
                    if(onProxyInterceptionEventListenerInterface.interceptRequests()){
                        return onProxyInterceptionEventListenerInterface.intercept(originalUri);
                    }else{
                        return originalUri;
                    }
                }
            });
        }
    }

    public interface OnProxyInterceptionEventListenerInterface{

        boolean interceptRequests();

        URI intercept(URI uri);
    }

    private static OnProxyInterceptionEventListenerInterface onProxyInterceptionEventListenerInterface = null;
    public static void setOnProxyInterceptionEventListenerInterface(OnProxyInterceptionEventListenerInterface listener){
        System.out.println("Event Listener Being Set In ClientRequestHandler");
        onProxyInterceptionEventListenerInterface = listener;
    }

    /**
     * makeRequest is the primary wrapper for executing web requests by the ClientRequestHandler
     * @param route - the URL route without the base url being called
     * @param request - the request being made
     * @param responseType - the object the response JSON will be serialized into
     * @param <T>
     * @return - The serialized class object representing the JSON response
     * @throws SecureMessengerClientException - A client side exception has occurred. Handling of the request or response has failed
     * @throws SecureMessengerException - A server side exception has occurred. The server returned an error, and the
     * client has handled it successfully.
     */
    public <T> T makeRequest(String route, SMRequestInterface request, Class<T> responseType) throws SecureMessengerClientException, SecureMessengerException{
        try{
            switch(request.getRequestMethod()){
                case POST:
                    return this.restTemplate.postForObject(baseURL + route, request, responseType, request.getRequestParams());
                case GET:
                    return this.restTemplate.getForObject(baseURL + route, responseType, request.getRequestParams());
                case PUT:
                    HttpEntity entity = request.getRequestAsEntity();
                    return this.restTemplate.exchange(baseURL + route, HttpMethod.PUT, entity, responseType, request.getRequestParams()).getBody();
                default:
                    throw new SecureMessengerClientException("Request Method Could Not Be Determined For Request");
            }
        }catch(HttpServerErrorException hsee){
            System.out.println(hsee.getResponseBodyAsString());
            System.out.println(hsee.getResponseHeaders().toString());

            String body = hsee.getResponseBodyAsString();
            try{
                ObjectMapper mapper = new ObjectMapper();
                ResponseStatus error = mapper.readValue(body, ResponseStatus.class);
                throw new SecureMessengerException(error, hsee.getRawStatusCode(), hsee.getResponseBodyAsString());
            }catch(IOException ioe){
                //do nothing so as to stick with default implementation
                throw new SecureMessengerClientException(ioe.getMessage());
            }

        }catch(HttpClientErrorException hcee){
            String body = hcee.getResponseBodyAsString();
            System.out.println(body);
            System.out.println(hcee.getResponseHeaders().toString());

            try{
                ObjectMapper mapper = new ObjectMapper();
                ResponseStatus error = mapper.readValue(body, ResponseStatus.class);
                throw new SecureMessengerException(error, hcee.getRawStatusCode(), hcee.getResponseBodyAsString());
            }catch(IOException ioe){
                //do nothing so as to stick with default implementation
                throw new SecureMessengerClientException(ioe.getMessage());
            }
        }
    }

    /**
     * set the client name header value. This is to uniquely identify the client on the Secure Messaging API.
     * @param clientName - the name of the client as a string
     */
    public void setClientName(String clientName){
        this.authInterceptor.setClientName(clientName);
    }

    /**
     * returns the version of the java sdk
     * @return the java sdk version as a string
     */
    public String getClientVersion(){
        return this.authInterceptor.getClientVersion();
    }

    /**
     * set the client version header value. This is to uniquely identify the version of the client on the Secure
     * Messaging API
     * @param clientVersion - the version of the client as a string
     */
    public void setClientVersion(String clientVersion){
        this.authInterceptor.setClientVersion(clientVersion);
    }

    /**
     * helper method to return the Spring Framework rest template client
     * @return
     */
    public RestTemplate getRestTemplate(){
        return this.restTemplate;
    }

    /**
     * helper method to return the header interceptor used for managing authentication with the Secure Messaging API
     * @return
     */
    public HeaderRequestInterceptor getAuthInterceptor() {
        return authInterceptor;
    }

    /**
     * helper method to return the base url set for communication with the Secure Messaging API
     * @return
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * set the session information for the ClientRequestHandler. This gives the client context as to where to call
     * the api and for whose account
     * @param session the session
     */
    public void setSession(Session session){
        this.clientSession = session;
        this.authInterceptor.addSessionToken(session.sessionToken);
    }

    /**
     * helper method to return the session object
     * @return
     */
    public Session getSession(){
        return this.clientSession;
    }
}
