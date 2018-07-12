package com.securemessaging.javamessenger.sm.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.javamessenger.sm.Message;
import com.securemessaging.javamessenger.sm.enums.BodyFormat;
import com.securemessaging.javamessenger.sm.enums.FyeoType;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSummaryResponse {


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class User{

        public String guid;
        public String email;
        public String firstName;
        public String lastName;
    }

    public String guid;
    public String parentGuid;
    public String campaignGuid;


    public ArrayList<User> to;
    public ArrayList<User> cc;
    public ArrayList<User> bcc;

    public String action; //convert this to the ActionCode Enum

    public String subject;
    public String body;
    public String bodyFormat; // convert this to BodyFormat Enum

    public User sender;
    //public String replyTo;

    public MessageSummaryResponse.Options messageOptions = new MessageSummaryResponse.Options();

    public class Options {
        /* Set default values for message */
        public boolean allowForward = true;
        public boolean allowReply = true;
        public boolean allowTracking = true;
        public String fyeoType; //convert this to FYEOType Enum
        public boolean shareTracking = true;
    }
}
