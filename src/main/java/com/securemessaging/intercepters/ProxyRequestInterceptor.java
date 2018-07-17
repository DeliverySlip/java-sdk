package com.securemessaging.intercepters;

import com.securemessaging.utils.BuildVersion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ProxyRequestInterceptor implements ClientHttpRequestInterceptor {

    private String baseUrl;
    private String path;

    public void setProxyBaseUrl(String baseUrl){
        this.baseUrl = baseUrl;
    }

    public void setProxyPath(String path){
        this.path = path;
    }


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URLOverridableHttpRequestWrapper wrapper = new URLOverridableHttpRequestWrapper(request);
        return execution.execute(wrapper, body);
    }

    private class URLOverridableHttpRequestWrapper extends HttpRequestWrapper{

        public URLOverridableHttpRequestWrapper(HttpRequest request) {
            super(request);
        }

        @Override
        public URI getURI() {
            try {
                return new URI(baseUrl + "/" + path);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
