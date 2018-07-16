package com.securemessaging.sm.request;

import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

public class GetMessageRequest implements SMRequestInterface {

    public String messageGuid;

    @Override
    public String getRequestRoute() {
        return "/messages/" + messageGuid;
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
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<GetMessageRequest>(this);
    }
}
