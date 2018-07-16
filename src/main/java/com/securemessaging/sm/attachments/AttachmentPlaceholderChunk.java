package com.securemessaging.sm.attachments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.sm.Attachment;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * AttachmentPlaceholderChunk is a dom object representing a single chunk within an attachment that has been
 * allocated but has not been uploaded to yet in the Secure Messaging API.
 */
public class AttachmentPlaceholderChunk{
    /**
     * The sequence number of the attachment Chunk.
     */
    public Integer chunkNumber = null;

    /**
     * The size in Bytes of of this Chunk only.
     */
    public Long bytesSize = null;

    /**
     * The start byte index that should be used to slice the chunk in relation to the entire Attachments byte stream.
     */
    public Long byteStartIndex = null;

    /**
     * The end byte index that should be used to slice the chunk in relation to the entire Attachments byte stream.
     */
    public Long byteEndIndex = null;

    /**
     * The URI that should be used to POST the chunk to.
     */
    public String uploadUri = null;

    public Integer getChunkNumber() { return chunkNumber; }
    public AttachmentPlaceholderChunk setChunkNumber(Integer value) { this.chunkNumber = value; return this; }
    public Long getBytesSize() { return bytesSize; }
    public AttachmentPlaceholderChunk setBytesSize(Long value) { this.bytesSize = value; return this; }
    public Long getByteStartIndex() { return byteStartIndex; }
    public AttachmentPlaceholderChunk setByteStartIndex(Long value) { this.byteStartIndex = value; return this; }
    public Long getByteEndIndex() { return byteEndIndex; }
    public AttachmentPlaceholderChunk setByteEndIndex(Long value) { this.byteEndIndex = value; return this; }
    public String getUploadUri() { return uploadUri; }
    public AttachmentPlaceholderChunk setUploadUri(String value) { this.uploadUri = value; return this; }
}
