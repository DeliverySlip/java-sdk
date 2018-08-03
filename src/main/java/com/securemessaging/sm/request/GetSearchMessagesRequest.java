package com.securemessaging.sm.request;

import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

public class GetSearchMessagesRequest implements SMRequestInterface {

    public String searchCriteria;
    public String types = "Inbox";

    public int pageSize;
    public int page;


    @Override
    public String getRequestRoute() {
        return "/messages/search?filter={filter}&page={pageNumber}&pageSize={pageSize}";
    }

    @Override
    public Map<String,String> getRequestParams() {
        HashMap map = new HashMap<String, String>();
        map.put("filter", "{ \"types\":[" + this.types + "], \"searchCriteria\": '" + this.searchCriteria + "'}");
        map.put("pageNumber", this.page);
        map.put("pageSize", this.pageSize);
        return map;

    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.GET;
    }

    @Override
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<GetSearchMessagesRequest>(this);
    }
}
