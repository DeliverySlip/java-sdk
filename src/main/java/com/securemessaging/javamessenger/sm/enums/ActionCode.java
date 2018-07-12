package com.securemessaging.javamessenger.sm.enums;

public enum ActionCode {
    NEW("New"), REPLY("Reply"), REPLYALL("ReplyAll"), FORWARD("Forward");

    private final String actionCode;

    ActionCode(String actionCode){
        this.actionCode = actionCode;
    }

    public String getEnumText(){
        return this.actionCode;
    }

    public static ActionCode enumFromEnumText(String enumText){
        if(enumText.equals("New")){
            return ActionCode.NEW;
        }else if(enumText.equals("Reply")){
            return ActionCode.REPLY;
        }else if(enumText.equals("ReplyAll")){
            return ActionCode.REPLYALL;
        }else if(enumText.equals("Forward")){
            return ActionCode.FORWARD;
        }
        return null;
    }
}
