package com.securemessaging.javamessenger.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetNewAuthenticationTokenResponse{

    public String authenticationToken;

}
