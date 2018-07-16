package com.securemessaging.sm.enums;

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
    public String getEnumText(){
        return this.fyeoType;
    }

    public static FyeoType enumFromEnumText(String enumText){
        if(enumText.equals("Disabled")){
            return FyeoType.DISABLED;
        }else if(enumText.equals("AccountPassword")){
            return FyeoType.ACCOUNTPASSWORD;
        }else if(enumText.equals("UniquePassword")){
            return FyeoType.UNIQUEPASSWORD;
        }
        return null;
    }

}
