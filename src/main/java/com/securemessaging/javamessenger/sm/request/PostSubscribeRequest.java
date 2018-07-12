package com.securemessaging.javamessenger.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostSubscribeRequest implements SMRequestInterface {


    public String notificationSessionToken;
    public ArrayList<Subscription> subscriptions;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Subscription{
        public String subscriptType;
        public String subscriptionGuid;
    }


    @Override
    public String getRequestRoute() {
        return "/notifications/subscription";
    }

    @Override
    public Map<String, String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.POST;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PostSubscribeRequest>(this);
    }
}
