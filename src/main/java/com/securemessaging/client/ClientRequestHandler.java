package com.securemessaging.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.intercepters.HeaderRequestInterceptor;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.Session;
import com.securemessaging.sm.response.meta.ResponseStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientRequestHandler is a wrapper for the Spring RestTemplate to make it easier and more portable to use and configure
 * around the Secure Messaging Client
 */
public class ClientRequestHandler {

    private RestTemplate restTemplate;
    private HeaderRequestInterceptor authInterceptor;

    private String baseURL;

    private Session clientSession;

    public ClientRequestHandler(String baseURL){

        this.baseURL = baseURL;

        restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        authInterceptor = new HeaderRequestInterceptor();
        interceptors.add(authInterceptor);
        restTemplate.setInterceptors(interceptors);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new FormHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        // Add the message converters to the restTemplate
        restTemplate.setMessageConverters(messageConverters);
    }

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

    public void setClientName(String clientName){
        this.authInterceptor.setClientName(clientName);
    }

    public String getClientVersion(){
        return this.authInterceptor.getClientVersion();
    }

    public void setClientVersion(String clientVersion){
        this.authInterceptor.setClientVersion(clientVersion);
    }


    public RestTemplate getRestTemplate(){
        return this.restTemplate;
    }

    public HeaderRequestInterceptor getAuthInterceptor() {
        return authInterceptor;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setSession(Session session){
        this.clientSession = session;
        this.authInterceptor.addSessionToken(session.sessionToken);
    }

    public Session getSession(){
        return this.clientSession;
    }
}
