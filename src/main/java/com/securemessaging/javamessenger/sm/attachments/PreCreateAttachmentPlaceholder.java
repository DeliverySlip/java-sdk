package com.securemessaging.javamessenger.sm.attachments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * PreCreateAttachmentPlaceholder is a dom object representing a placeholder / attachment meta for a single
 * attachment file. This placeholder represents a file that has not had resources allocated for it to be
 * uploaded to via the Secure Messaging API. PreCreateAttachmentPlaceholder is used when precreating an attachment
 * and is required as an array of them when creating a PostPreCreateAttachmentsRequest object
 */
public class PreCreateAttachmentPlaceholder{

    private String fileName = null;
    private Long totalBytesLength = null;

    public PreCreateAttachmentPlaceholder(String fileName, Long totalBytesLength){
        this.fileName = fileName;
        this.totalBytesLength = totalBytesLength;
    }


    public String getFileName() { return fileName; }
    public PreCreateAttachmentPlaceholder setFileName(String value) { this.fileName = value; return this; }

    public Long getTotalBytesLength() { return totalBytesLength; }
    public PreCreateAttachmentPlaceholder setTotalBytesLength(Long value) { this.totalBytesLength = value; return this; }

}
