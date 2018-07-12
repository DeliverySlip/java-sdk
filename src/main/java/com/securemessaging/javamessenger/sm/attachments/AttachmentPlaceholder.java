package com.securemessaging.javamessenger.sm.attachments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentPlaceholder{

    public String attachmentGuid = null;
    public String fileName = null;

    /**
     * The placeholders for the Chunks of an Attachment. Use this information to seperate the Attachment into Chunks.
     * Attachments that are smaller than the Chunk Byte Size will only require 1 Chunk. An Attachments
     * Chunk Numbers are in sequence (eg. 1,2,3,4) and always starts at 1.
     */
    public ArrayList<AttachmentPlaceholderChunk> chunks = null;

    public String getAttachmentGuid() { return attachmentGuid; }
    public AttachmentPlaceholder setAttachmentGuid(String value) { this.attachmentGuid = value; return this; }
    public String getFileName() { return fileName; }
    public AttachmentPlaceholder setFileName(String value) { this.fileName = value; return this; }
    public ArrayList<AttachmentPlaceholderChunk> getChunks() { return chunks; }
    public AttachmentPlaceholder setChunks(ArrayList<AttachmentPlaceholderChunk> value) { this.chunks = value; return this; }
}
