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

    private OnRequestInterceptionEventHandler onRequestInterceptionEventHandler;
    public void setOnRequestInterceptionEventHandler(OnRequestInterceptionEventHandler listener){
        this.onRequestInterceptionEventHandler = listener;
    }

    public interface OnRequestInterceptionEventHandler{
        URI getProxyPath(URI originalUri);
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        URLOverridableHttpRequestWrapper wrapper = new URLOverridableHttpRequestWrapper(request);

        if(this.onRequestInterceptionEventHandler != null){
            URI newURI = this.onRequestInterceptionEventHandler.getProxyPath(request.getURI());
            wrapper.setURI(newURI);
        }

        return execution.execute(wrapper, body);
    }

    private class URLOverridableHttpRequestWrapper extends HttpRequestWrapper{

        public URLOverridableHttpRequestWrapper(HttpRequest request) {
            super(request);
            uriOverride = request.getURI();
        }


        private URI uriOverride;

        public void setURI(URI uri){
            this.uriOverride = uri;
        }

        @Override
        public URI getURI() {
            return this.uriOverride;
        }
    }
}
