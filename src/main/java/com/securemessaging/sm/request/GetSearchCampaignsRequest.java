package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GetSearchCampaignsRequest implements SMRequestInterface {

    public String searchCriteria;
    public String status;
    public List<String> campaignGuids = null;

    public int pageSize = 25;
    public int page = 1;


    @Override
    public String getRequestRoute() {
        return "/campaigns/search?filter={filter}&page={pageNumber}&pageSize={pageSize}";
    }

    @Override
    public boolean requestRouteHasApiPath() {
        return false;
    }

    @Override
    public Map<String,String> getRequestParams() {
        HashMap map = new HashMap<String, String>();

        StringBuilder builder = new StringBuilder();
        for(String campaignGuid: campaignGuids){
            builder.append("'");
            builder.append(campaignGuid);
            builder.append("',");
        }

        if(builder.length() > 0){
            builder.deleteCharAt(builder.length() - 1);
        }

        map.put("filter", "{ \"status\":'" + this.status + "', \"campaignGuids\":[" + builder.toString() + "], \"searchCriteria\": '" + this.searchCriteria + "'}");
        map.put("pageNumber", this.page);
        map.put("pageSize", this.pageSize);
        return map;
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.GET;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<GetSearchCampaignsRequest>(this);
    }
}
