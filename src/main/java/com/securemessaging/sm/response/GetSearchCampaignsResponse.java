package com.securemessaging.sm.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.campaign.Campaign;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSearchCampaignsResponse {

    public int pageSize;
    public int totalPages;
    public int totalItems;
    public int currentPage;


    public ArrayList<Campaign> results = new ArrayList<Campaign>();
}
