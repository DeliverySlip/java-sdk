package com.securemessaging.sm.enums.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum NotificationType {
    MESSAGE("Message"), ATTACHMENT("Attachment"), ESIGNATURE("Esignature"), TRACKING("Tracking");

    private final String newAssetNotificationFilter;

    NotificationType(String newAssetNotificationFilter){
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
    public static NotificationType enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(NotificationType validEnumValue: NotificationType.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }


}
