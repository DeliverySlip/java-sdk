package com.securemessaging.sm.auth;

import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Session;
import com.securemessaging.SMRequestInterface;
import com.securemessaging.sm.request.GetUserSettingsRequest;
import com.securemessaging.sm.response.GetUserSettingsResponse;

/**
 * SessionFactory creates Session objects so that the ClientRequestHandler has context on what and where to make its
 * API calls to
 */
public class SessionFactory {

    public static Session createSession(SMAuthenticationInterface authentication, ClientRequestHandler client) throws SecureMessengerClientException, SecureMessengerException{

        //create the session however appropriate
        SMRequestInterface requestBody = authentication.getRequestBody();
        Session session = client.makeRequest(requestBody.getRequestRoute(), requestBody, Session.class);

        // then get client information aswell to link with the session information
        client.setSession(session);
        GetUserSettingsRequest settingsRequestBody = new GetUserSettingsRequest();
        GetUserSettingsResponse settingsResponse = client.makeRequest(settingsRequestBody.getRequestRoute(), settingsRequestBody, GetUserSettingsResponse.class);

        session.firstName = settingsResponse.firstName;
        session.lastName = settingsResponse.lastName;
        session.emailAddress = settingsResponse.emailAddress;
        session.state = settingsResponse.state;

        return session;
    }
}
