package com.securemessaging.campaign;

import com.securemessaging.Message;

public class CampaignMessage extends Message {

    private String campaignGuid;

    public String getCampaignGuid(){
        return campaignGuid;
    }

    public void setCampaignGuid(String campaignGuid){
        this.campaignGuid = campaignGuid;
    }


}
