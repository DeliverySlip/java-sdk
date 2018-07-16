package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCreateNotificationSessionRequest implements SMRequestInterface {

    public String deviceType = "inApp";
    //public String clientAppId = "java-secure-messenger";
    //public String pushToken;
    public boolean enableUserTrackingNotifications;
    public ArrayList<String> newAssetNotificationFilters;


    @Override
    public String getRequestRoute() {
        return "/notifications";
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
        return new HttpEntity<PostCreateNotificationSessionRequest>(this);
    }
}
