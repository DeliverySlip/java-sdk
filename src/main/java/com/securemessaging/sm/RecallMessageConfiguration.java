package com.securemessaging.sm;

public class RecallMessageConfiguration {

    private String messageGuid;
    private String recallReason = "";
    private boolean singleRecall = false;
    private boolean unshareAttachments = true;

    public RecallMessageConfiguration(String messageGuid){
        this.messageGuid = messageGuid;
    }

    public String getMessageGuid(){
        return this.messageGuid;
    }

    public void setRecallReason(String recallReason){
        this.recallReason = recallReason;
    }

    public String getRecallReason(){
        return this.recallReason;
    }

    public void setSingleRecall(boolean singleRecall){
        this.singleRecall = singleRecall;
    }

    public boolean getSingleRecall(){
        return this.singleRecall;
    }

    public boolean setUnshareAttachments(boolean unshareAttachments){
        return this.unshareAttachments = unshareAttachments;
    }

    public boolean getUnshareAttachments(){
        return this.unshareAttachments;
    }
}
