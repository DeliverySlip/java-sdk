package com.securemessaging.sm.enums.notifications;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum EventType {
    NEWASSETPAYLOAD("NewAssetPayload"), USERTRACKINGPAYLOAD("UserTrackingPayloed");

    private final String eventType;

    EventType(String eventType){
        this.eventType = eventType;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    @JsonValue
    public String getEnumText(){
        return this.eventType;
    }

    @JsonCreator
    public static EventType enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(EventType validEnumValue: EventType.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }

    public static boolean isValidEventTypeText(String enumText){
        try{
            EventType.enumFromEnumText(enumText);
            return true;
        }catch(SecureMessengerClientException smce){
            return false;
        }
    }
}
