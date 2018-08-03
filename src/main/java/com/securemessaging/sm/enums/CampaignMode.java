package com.securemessaging.sm.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum CampaignMode {
    AUTOMATIC("Automatic"), MANUAL("Manual");

    private final String campaignMode;

    CampaignMode(String campaignMode){
        this.campaignMode = campaignMode;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    @JsonValue
    public String getEnumText(){
        return this.campaignMode;
    }

    @JsonCreator
    public static CampaignMode enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(CampaignMode validEnumValue: CampaignMode.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }
}
