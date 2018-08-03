package com.securemessaging.sm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.sm.enums.ActionCode;
import lombok.Data;


/**
 * PreCreateConfiguration represents a domain object of the preCreate data sent to pre-create a message
 */
public class PreCreateConfiguration {


    //request
    private ActionCode actionCode;
    private String parentGuid;
    private String password;


    /**
     * setActionCode sets the action code based on the options available from the ActionCode enum in the PreCreateConfiguration object.
     * ActionCode can either be NEW, REPLY, REPLYALL or FORWARD.
     * @param actionCode the actionCode enum value
     */
    public void setActionCode(ActionCode actionCode){
        this.actionCode = actionCode;
    }

    public ActionCode getActionCode(){
        return this.actionCode;
    }

    /**
     * setParentGuid sets the ParentGuid value of the preCreate. This is required if the user sets the action code to
     * REPLY, REPLYALL or FORWARD
     * @param parentGuid the guid of the parent message
     */
    public void setParentGuid(String parentGuid){
        this.parentGuid = parentGuid;
    }

    public String getParentGuid(){
        return this.parentGuid;
    }

    /**
     * setPassword sets the password. This is if the parent message required a password to be accessed.
     * @param password the password to open the parent message belonging to the parent guid
     */
    public void setPassword(String password){
        this.password = password;
    }

    public String getPassword(){
        return this.password;
    }

}
