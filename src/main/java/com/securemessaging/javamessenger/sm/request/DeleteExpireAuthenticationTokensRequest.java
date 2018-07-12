package com.securemessaging.javamessenger.sm.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

// delete / expire all authentication tokens of user
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteExpireAuthenticationTokensRequest implements SMRequestInterface {
    @Override
    public String getRequestRoute() {
        return "/user/authenticationtokens";
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
        return new HttpEntity<DeleteExpireAuthenticationTokensRequest>(this);
    }
}
