package com.securemessaging.sm.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

import javax.swing.*;
import java.security.InvalidParameterException;

public enum ActionCode {
    NEW("New"), REPLY("Reply"), REPLYALL("ReplyAll"), FORWARD("Forward");

    private final String actionCode;

    ActionCode(String actionCode){
        this.actionCode = actionCode;
    }

    @JsonValue
    public String getEnumText(){
        return this.actionCode;
    }

    @JsonCreator
    public static ActionCode enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(ActionCode validEnumValue: ActionCode.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }
}
