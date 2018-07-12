package com.securemessaging.javamessenger.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PutSendMessageResponse {

    public String notificationBodyText;
    public String notificationBodyHtml;
    public boolean notificationSent;

}
