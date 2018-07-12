package com.securemessaging.javamessenger.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.sm.attachments.AttachmentPlaceholder;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostPreCreateAttachmentsResponse {

    public ArrayList<AttachmentPlaceholder> attachmentPlaceholders;

}
