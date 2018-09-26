package com.securemessaging.ccc.request;

import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

public class PublicGetServiceRequest implements SMRequestInterface {
    @Override
    public String getRequestRoute() {
        return "/public/services/single";
    }

    @Override
    public boolean requestRouteHasApiPath() {
        return false;
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
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PublicGetServiceRequest>(this);
    }
}
