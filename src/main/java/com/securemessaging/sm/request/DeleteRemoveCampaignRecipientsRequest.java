package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteRemoveCampaignRecipientsRequest implements SMRequestInterface {

    public String campaignGuid;
    public List<String> emailAddresses = null;
    public boolean removeAll = false;
    public boolean recallMessagesSent = false;
    public String recallMessage = "";

    @Override
    public String getRequestRoute() {
        String baseRoute = "/campaign/{campaignGuid}/recipients?removeAll={removeAll}&recallMessageSent={recallMessageSent}?" +
                "recallMessage={recallMessage}";

        if(emailAddresses != null){
            baseRoute += "?emailAddresses={emailAddresses}";
        }

        return baseRoute;
    }

    @Override
    public boolean requestRouteHasApiPath() {
        return false;
    }

    @Override
    public Map<String, String> getRequestParams() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("campaignGuid", campaignGuid);
        map.put("removeAll", String.valueOf(removeAll));
        map.put("recallMessageSent", String.valueOf(recallMessagesSent));
        map.put("recallMessage", recallMessage);

        if(emailAddresses != null){
            StringBuilder builder = new StringBuilder();

            for(String emailAddress: emailAddresses){
                builder.append(emailAddress);
                builder.append(",");
            }

            builder.deleteCharAt(builder.length() - 1);

            map.put("emailAddresses", builder.toString());
        }

        return map;
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.DELETE;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<DeleteRemoveCampaignRecipientsRequest>(this);
    }
}
