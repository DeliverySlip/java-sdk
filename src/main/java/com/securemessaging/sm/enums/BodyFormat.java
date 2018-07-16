package com.securemessaging.sm.enums;

public enum BodyFormat {
    TEXT("Text"), HTML("Html");

    private final String bodyFormat;

    BodyFormat(String bodyFormat){
        this.bodyFormat = bodyFormat;
    }

    /**
     * getEnumText gets the value of the enum which is required to be passed to the server
     * @return the value of the enum
     */
    public String getEnumText(){
        return this.bodyFormat;
    }

    public static BodyFormat enumFromEnumText(String enumText){
        if(enumText.equals("Text")){
            return BodyFormat.TEXT;
        }else if(enumText.equals("Html")){
            return BodyFormat.HTML;
        }
        return null;
    }

}
