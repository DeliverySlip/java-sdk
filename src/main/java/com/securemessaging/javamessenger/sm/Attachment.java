package com.securemessaging.javamessenger.sm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.sm.attachments.AttachmentPlaceholder;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Attachment is a dom object representing an attachment that has had resources allocated for it on the server and
 * is ready to be uploaded.
 */
public class Attachment {

    public ArrayList<AttachmentPlaceholder> attachmentPlaceholders = null;
    public ArrayList<AttachmentPlaceholder> getAttachmentPlaceholders() { return attachmentPlaceholders; }


}
