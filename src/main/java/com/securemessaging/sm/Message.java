package com.securemessaging.sm;
import com.securemessaging.sm.enums.BodyFormat;
import com.securemessaging.sm.enums.FyeoType;

public class Message {

    //meta information for the client SDK
    public boolean hasBeenSaved = false;

    private String messageGuid;
    private String password;
    private boolean inviteNewUsers = true;
    private boolean sendNotification = true;
    private String craCode;


    private String[] to;
    private String[] from;
    private String[] cc;
    private String[] bcc;
    private String subject;
    private String body;
    private String bodyFormat = BodyFormat.TEXT.getEnumText();

    private Options messageOptions = new Options();

    private class Options {
        /* Set default values for message */
        private boolean allowForward = true;
        private boolean allowReply = true;
        private boolean allowTracking = true;
        private String fyeoType = FyeoType.DISABLED.getEnumText();
        private boolean shareTracking = true;
    }

    /**
     * getMessageGuid gets the Guid belonging to the message which was assigned to it upon pre-creating the message
     * @return String the message guid
     */
    public String getMessageGuid(){
        return this.messageGuid;
    }

    /**
     * setMessageGuid sets the Guid belonging to the message
     * @param guid String the guid to be set for the message
     */
    public void setMessageGuid(String guid){
        this.messageGuid = guid;
    }

    /**
     * setTo sets who the message will be sent to
     * @param to String the email of the recipient of the message (eg email@domain.com)
     */
    public void setTo(String[] to){
        this.to = to;
    }

    public String[] getTo(){
        return this.to;
    }

    /**
     * setFrom sets who the message was sent from
     * @param from String the email of the sender of the message (eg email@domain.com)
     */
    public void setFrom(String[] from){
        this.from = from;
    }

    public String[] getFrom(){
        return this.from;
    }

    /**
     * setCC sets the CC recipients of the message. The recipients must be in an array
     * @param cc String[] an array of recipients who will be CC'd the message sent
     */
    public void setCC(String[] cc){
        this.cc = cc;
    }

    public String[] getCC(){
        return this.cc;
    }

    /**
     * setBCC sets the BCC recipients of the message. The recipients must be in an array
     * @param bcc String[] an array of recipients who will be BCC'd the message sent
     */
    public void setBCC(String[] bcc){
        this.bcc = bcc;
    }

    public String[] getBCC(){
        return this.bcc;
    }

    /**
     * setSubject sets the subject of the message
     * @param subject String the subject of the message
     */
    public void setSubject(String subject){
        this.subject = subject;
    }

    public String getSubject(){
        return this.subject;
    }

    /**
     * setBody sets the body of the message
     * @param body String the body of the message
     */
    public void setBody(String body){
        this.body = body;
    }

    public String getBody(){
        return this.body;
    }

    /**
     * setBodyFormat sets the format of the body content being sent. This can either be HTML or TEXT as specified in the
     * BodyFormat object available through the message object. (eg Message.BodyFormat.HTML / Messaage.BodyFormat.TXT)
     * @param bodyFormat BodyFormat the bodyFormat enum of HTML or TXT
     */
    public void setBodyFormat(BodyFormat bodyFormat){
        this.bodyFormat = bodyFormat.getEnumText();
    }

    public BodyFormat getBodyFormat(){
        return BodyFormat.enumFromEnumText(this.bodyFormat);
    }

    /**
     * setForward sets the permission of whether the recipients can forward the email. By default this is enabled
     * @param enabled Boolean the state of whether forwarding is enabled or disabled
     */
    public void setForward(boolean enabled){
        this.messageOptions.allowForward = enabled;
    }

    public boolean getForwardStatus(){
        return this.messageOptions.allowForward;
    }

    /**
     * setReply sets the permission fo whether the recipients can reply to the email. By default this is enabled
     * @param enabled Boolean the state of whether replying is enabled or disabled
     */
    public void setReply(boolean enabled){
        this.messageOptions.allowReply = enabled;
    }

    public boolean getReplyStatus(){
        return this.messageOptions.allowReply;
    }

    /**
     * setTracking sets the permission of whether to enable tracking. By default this is enabled
     * @param enabled Boolean the state of whether tracking is enabled or not
     */
    public void setTracking(boolean enabled){
        this.messageOptions.allowTracking = enabled;
    }

    public boolean getTrackingStatus(){
        return this.messageOptions.allowTracking;
    }

    /**
     * setShareTracking sets the permission of whether the recipients are able to view tracking. By default this is
     * enabled
     * @param enabled Boolean the state of whether share tracking is enabled or not
     */
    public void setShareTracking(boolean enabled){
        this.messageOptions.shareTracking = enabled;
    }

    public boolean getShareTrackingStatus(){
        return this.messageOptions.shareTracking;
    }

    /**
     * setFyeoType sets the type of FYEO message this is that is being sent. By default this is set to DISABLED. This
     * can either be DISABLED, ACCOUNTPASSWORD or UNIQUEPASSWORD as specified in the FyeoType enum available through
     * the message object.
     * @param type FyeoType the FyeoType enum representing the FyeoType
     */
    public void setFyeoType(FyeoType type){
        this.messageOptions.fyeoType = type.getEnumText();
    }

    public FyeoType getFyeoType(){
        return FyeoType.enumFromEnumText(this.messageOptions.fyeoType);
    }

    public void setCraCode(String craCode){
        this.craCode = craCode;
    }

    public String getCraCode(){
        return this.craCode;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setInviteNewUsers(boolean inviteNewUsers){
        this.inviteNewUsers = inviteNewUsers;
    }

    public boolean getInviteNewUsersStatus(){
        return this.inviteNewUsers;
    }

    public void setSendNotification(boolean sendNotification){
        this.sendNotification = sendNotification;
    }

    public boolean getSendNotificationStatus(){
        return this.sendNotification;
    }


}
