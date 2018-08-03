package com.securemessaging.sm.enums.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum SubscriptionType {
    MESSAGE("Message"), ATTACHMENT("Attachment");

    private final String newAssetNotificationFilter;

    SubscriptionType(String newAssetNotificationFilter){
        this.newAssetNotificationFilter = newAssetNotificationFilter;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    @JsonValue
    public String getEnumText(){
        return this.newAssetNotificationFilter;
    }

    @JsonCreator
    public static SubscriptionType enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(SubscriptionType validEnumValue: SubscriptionType.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }
}
