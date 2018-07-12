package com.securemessaging.javamessenger.sm.enums.notifications;

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
    public String getEnumText(){
        return this.newAssetNotificationFilter;
    }

    public static SubscriptionType enumFromEnumText(String enumText){
        if(enumText.equals("Message")){
            return SubscriptionType.MESSAGE;
        }else if(enumText.equals("Attatchment")){
            return SubscriptionType.ATTACHMENT;
        }

        return null;
    }
}
