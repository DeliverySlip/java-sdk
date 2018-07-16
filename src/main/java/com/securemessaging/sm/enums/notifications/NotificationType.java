package com.securemessaging.sm.enums.notifications;

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
    public String getEnumText(){
        return this.newAssetNotificationFilter;
    }

    public static NotificationType enumFromEnumText(String enumText){
        if(enumText.equals("Message")){
            return NotificationType.MESSAGE;
        }else if(enumText.equals("Attatchment")){
            return NotificationType.ATTACHMENT;
        }else if(enumText.equals("Esignature")){
            return NotificationType.ESIGNATURE;
        }else if(enumText.equals("Tracking")){
            return NotificationType.TRACKING;
        }
        return null;
    }


}
