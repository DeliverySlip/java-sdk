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
public class PutSendMessageRequest implements SMRequestInterface {

    public String messageGuid;
    public String password;
    public boolean inviteNewUsers;
    public boolean sendEmailNotification;
    public String craCode;
    public String[] notificationFormats;

    @Override
    public String getRequestRoute() {
        return "/messages/" + messageGuid + "/send";
    }

    @Override
    public boolean requestRouteHasApiPath() {
        return false;
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.PUT;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PutSendMessageRequest>(this);
    }
}
