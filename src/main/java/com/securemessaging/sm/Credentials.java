package com.securemessaging.sm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.sm.auth.SMAuthenticationInterface;
import com.securemessaging.sm.request.PostAuthenticateRequest;
import com.securemessaging.sm.request.PostLoginRequest;
import com.securemessaging.SMRequestInterface;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Credentials is a client class for handling user credentials information for creating sessions and
 * authenticating with the server
 */
public class Credentials implements SMAuthenticationInterface{

    private String username = null;
    private String password = null;
    private String authenticationToken = null;

    public Credentials(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Credentials(String authenticationToken){
        this.authenticationToken = authenticationToken;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getAuthenticationToken(){ return this.authenticationToken; }

    @Override
    public SMRequestInterface getRequestBody() throws SecureMessengerClientException {

        if(this.username != null && this.password != null){
            PostLoginRequest request = new PostLoginRequest();
            request.password = this.password;
            request.username = this.username;
            return request;
        }else if(this.authenticationToken != null){
            PostAuthenticateRequest request = new PostAuthenticateRequest();
            request.authenticationToken = this.authenticationToken;
            return request;
        }else{
            throw new SecureMessengerClientException("Credentials Were Not Instantiated Properly. " +
                    "Username & Password OR AuthenticationToken must be provided");
        }
    }
}
