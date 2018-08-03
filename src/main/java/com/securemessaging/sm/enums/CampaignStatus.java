package com.securemessaging.sm.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum CampaignStatus {
    NOTSTARTED("NotStarted"), INPROGRESS("InProgress"), STOPPED("Stopped"), FAILED("Failed"), COMPLETED("Completed");

    private final String campaignStatus;

    CampaignStatus(String campaignMode){
        this.campaignStatus = campaignMode;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    @JsonValue
    public String getEnumText(){
        return this.campaignStatus;
    }

    @JsonCreator
    public static CampaignStatus enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(CampaignStatus validEnumValue: CampaignStatus.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }
}
