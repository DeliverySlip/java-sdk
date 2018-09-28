package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPingRequest implements SMRequestInterface {


    @Override
    public String getRequestRoute() {
        return "/api/public/ping";
    }

    @Override
    public boolean requestRouteHasApiPath() {
        return true;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.GET;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<GetPingRequest>(this);
    }
}
