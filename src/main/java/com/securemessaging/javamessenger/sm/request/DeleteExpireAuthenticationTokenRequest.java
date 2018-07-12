package com.securemessaging.javamessenger.sm.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

// delete / expire current SINGLE authentication token
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteExpireAuthenticationTokenRequest implements SMRequestInterface {

    public String authenticationToken;


    @Override
    public String getRequestRoute() {
        return "/user/authenticationtoken?authenticationToken=" + this.authenticationToken;
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.DELETE;
    }

    @Override
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<DeleteExpireAuthenticationTokenRequest>(this);
    }
}
