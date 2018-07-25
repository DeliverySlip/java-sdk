package com.securemessaging.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostConvertToESignatureResponse {
    public String newAttachmentGuid;
}
