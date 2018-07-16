package com.securemessaging.sm.enums;


public enum MessageBoxType {
    DRAFT("Draft"), INBOX("Inbox"), SENT("Sent"), TRASH("Trash");

    private final String messageBoxType;

    MessageBoxType(String messageBoxType){
        this.messageBoxType = messageBoxType;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    public String getEnumText(){
        return this.messageBoxType;
    }

    public static MessageBoxType enumFromEnumText(String enumText){
        if(enumText.equals("Draft")){
            return MessageBoxType.DRAFT;
        }else if(enumText.equals("Inbox")){
            return MessageBoxType.INBOX;
        }else if(enumText.equals("Sent")){
            return MessageBoxType.SENT;
        }else if(enumText.equals("Trash")){
            return MessageBoxType.TRASH;
        }
        return null;
    }
}
