package com.securemessaging.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteAttachmentRequest implements SMRequestInterface {

    public String attachmentGuid;

    @Override
    public String getRequestRoute() {
        return "/attachments/" + this.attachmentGuid;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.DELETE;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<DeleteAttachmentRequest>(this);
    }
}
