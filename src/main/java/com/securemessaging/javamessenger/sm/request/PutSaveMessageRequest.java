package com.securemessaging.javamessenger.sm.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.SMRequestInterface;
import com.securemessaging.javamessenger.sm.enums.BodyFormat;
import com.securemessaging.javamessenger.sm.enums.FyeoType;
import com.securemessaging.javamessenger.sm.enums.SMRequestMethod;
import lombok.Data;
import org.springframework.http.HttpEntity;

import java.util.HashMap;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutSaveMessageRequest implements SMRequestInterface {

    public String messageGuid;
    public String[] to;
    public String[] cc;
    public String[] bcc;
    public String subject;
    public String body;
    public String bodyFormat = BodyFormat.TEXT.getEnumText();

    public MessageOptions messageOptions = new MessageOptions();

    public static class MessageOptions {
        /* Set default values for message */
        public boolean allowForward = true;
        public boolean allowReply = true;
        public boolean allowTracking = true;
        public String fyeoType = FyeoType.DISABLED.getEnumText();
        public boolean shareTracking = true;

        public void setFYEOType(FyeoType fyeoType){
            this.fyeoType = fyeoType.getEnumText();
        }
    }

    public void setBodyFormat(BodyFormat bodyFormat){
        this.bodyFormat = bodyFormat.getEnumText();
    }

    @Override
    public String getRequestRoute() {
        return "/messages/" + this.messageGuid + "/save";
    }

    @Override
    public Map<String,String> getRequestParams() {
        return new HashMap<String, String>();
    }

    @Override
    public SMRequestMethod getRequestMethod() {
        return SMRequestMethod.PUT;
    }

    @Override
    @JsonIgnore
    public HttpEntity<?> getRequestAsEntity() {
        return new HttpEntity<PutSaveMessageRequest>(this);
    }
}
