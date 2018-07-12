package com.securemessaging.javamessenger.sm.enums.notifications;

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
    public String getEnumText(){
        return this.eventType;
    }

    public static EventType enumFromEnumText(String enumText){
        if(enumText.equals("NewAssetPayload")){
            return EventType.NEWASSETPAYLOAD;
        }else if(enumText.equals("UserTrackingPayload")){
            return EventType.USERTRACKINGPAYLOAD;
        }

        return null;
    }

    public static boolean isValidEventTypeText(String enumText){
        return EventType.enumFromEnumText(enumText) != null;
    }
}
