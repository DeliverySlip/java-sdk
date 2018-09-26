package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostAddCampaignRecipientsRequest implements SMRequestInterface {

    public String campaignGuid;

    //max length can only be 500
    public String[] emailAddresses = new String[500];


    @Override
    public String getRequestRoute() {
        return "/campaigns/" + campaignGuid + "/recipients";
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
        return SMRequestMethod.POST;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PostAddCampaignRecipientsRequest>(this);
    }
}
