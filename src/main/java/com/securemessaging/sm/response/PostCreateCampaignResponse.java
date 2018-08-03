package com.securemessaging.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostCreateCampaignResponse {

    public String campaignGuid;
    public String templateMessageGuid;
}
