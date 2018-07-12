package com.securemessaging.javamessenger.sm;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * AttachmentChunk is a domain object representing a piece of an attachment that needs to be uploaded. The Secure Messaging API
 * uploads its attachments in chunks and this object represents a single chunk during the upload process
 */
public class AttachmentChunk {


    public String attachmentGuid = null;

    /**
     * The sequence number of the attachment Chunk.
     */

    public Integer chunkNumber = null;

    public String getAttachmentGuid() { return attachmentGuid; }
    public AttachmentChunk setAttachmentGuid(String value) { this.attachmentGuid = value; return this; }
    public Integer getChunkNumber() { return chunkNumber; }
    public AttachmentChunk setChunkNumber(Integer value) { this.chunkNumber = value; return this; }

}
