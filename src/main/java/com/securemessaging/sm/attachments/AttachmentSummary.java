package com.securemessaging.sm.attachments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttachmentSummary{
    public String guid;
    public String fileName;
    public String type;
    public int fileSize;
    public String status;
    public int chunkCount;
    public String originalFileExtension;
}
