package com.securemessaging.javamessenger.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import com.securemessaging.javamessenger.sm.enums.ActionCode;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostPreCreateMessageRequest implements SMRequestInterface {

    public String actionCode;
    public String parentGuid;
    public String password;
    public String authAuditToken;
    public String campainGuid;
    public String externalMessageId;


    public void setActionCode(ActionCode actionCode){
        this.actionCode = actionCode.getEnumText();
    }


    @Override
    public String getRequestRoute() {
        return "/messages";
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.POST;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PostPreCreateMessageRequest>(this);
    }
}
