package com.securemessaging.javamessenger.sm.notifications.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewAssetPayload implements EventDataInterface{

    public String Type;
    public String Count;
    public String Timestamp;


}
