package com.securemessaging.sm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Session is a domain object for capturing the reply to login and recieve the authentication token.
 */
public class Session {

    public String sessionToken;
    public String firstName;
    public String lastName;
    public String state;
    public String emailAddress;
}
