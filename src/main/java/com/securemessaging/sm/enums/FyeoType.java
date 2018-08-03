package com.securemessaging.sm.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.securemessaging.ex.SecureMessengerClientException;

public enum FyeoType {
    DISABLED("Disabled"), ACCOUNTPASSWORD("AccountPassword"), UNIQUEPASSWORD("UniquePassword");

    private final String fyeoType;

    FyeoType(String fyeoType){
        this.fyeoType = fyeoType;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    @JsonValue
    public String getEnumText(){
        return this.fyeoType;
    }

    @JsonCreator
    public static FyeoType enumFromEnumText(String enumText) throws SecureMessengerClientException {
        for(FyeoType validEnumValue: FyeoType.values()){
            if(enumText.equals(validEnumValue.getEnumText())){
                return validEnumValue;
            }
        }

        throw new SecureMessengerClientException("enumText Does Not Match A Valid Enum");
    }

}
