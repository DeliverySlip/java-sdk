package com.securemessaging.sm.enums;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

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
    @JsonValue
    public String getEnumText(){
        return this.messageBoxType;
    }

    @JsonCreator
    public static MessageBoxType enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(MessageBoxType validEnumValue: MessageBoxType.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }
}
