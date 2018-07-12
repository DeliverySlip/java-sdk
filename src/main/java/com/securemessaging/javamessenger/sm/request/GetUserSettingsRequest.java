package com.securemessaging.javamessenger.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserSettingsRequest implements SMRequestInterface {




    @Override
    public String getRequestRoute() {
        return "/user/settings";
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.GET;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<GetUserSettingsRequest>(this);
    }
}
