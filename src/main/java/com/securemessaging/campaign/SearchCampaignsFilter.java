package com.securemessaging.campaign;

import com.securemessaging.sm.enums.CampaignStatus;

import java.util.List;

public class SearchCampaignsFilter {

    private String searchCriteria = null;
    private CampaignStatus status = null;
    private List<String> campaignGuids = null;

    public void setSearchCriteria(String searchCriteria){
        this.searchCriteria = searchCriteria;
    }

    public String getSearchCriteria(){
        return this.searchCriteria;
    }

    public void setCampaignStatus(CampaignStatus status){
        this.status = status;
    }

    public CampaignStatus getCampaignStatus(){
        return this.status;
    }

    public void setCampaignGuids(List<String> campaignGuids){
        this.campaignGuids = campaignGuids;
    }

    public List<String> getCampaignGuids(){
        return this.campaignGuids;
    }


}
