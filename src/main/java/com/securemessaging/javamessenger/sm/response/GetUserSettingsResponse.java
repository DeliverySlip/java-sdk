package com.securemessaging.javamessenger.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetUserSettingsResponse {


    public String firstName;
    public String lastName;
    public String emailAddress;
    public String state;

}
