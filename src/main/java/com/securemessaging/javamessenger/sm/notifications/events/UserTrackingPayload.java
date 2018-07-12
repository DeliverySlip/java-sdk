package com.securemessaging.javamessenger.sm.notifications.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTrackingPayload implements EventDataInterface{

    public boolean Context;
    public String UserEmail;
    public String UserName;
    public String AssetGuid;
    public String AssetName;
    public String Type;
    public String Timestamp;
    public String RelatedMessageGuid;

}
