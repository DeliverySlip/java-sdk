package com.securemessaging.sm.request;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.attachments.PreCreateAttachmentPlaceholder;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * PostPreCreateAttachmentsRequest is a dom object representing an attachment that has not been allocated resources for storage
 * on the Secure Messaging API.
 */
public class PostPreCreateAttachmentsRequest implements SMRequestInterface {

    @Override
    public String getRequestRoute() {
        return "/attachments/precreate";
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.POST;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PostPreCreateAttachmentsRequest>(this);
    }

    //request payload
    public String messageGuid = null;
    public ArrayList<PreCreateAttachmentPlaceholder> attachmentPlaceholders = null;


    public String getMessageGuid() { return messageGuid; }
    public PostPreCreateAttachmentsRequest setMessageGuid(String value) { this.messageGuid = value; return this; }

    public ArrayList<PreCreateAttachmentPlaceholder> getAttachmentPlaceholders() { return attachmentPlaceholders; }

    public PostPreCreateAttachmentsRequest setAttachmentPlaceholders(ArrayList<PreCreateAttachmentPlaceholder> value) { this.attachmentPlaceholders = value; return this; }
}
