package com.securemessaging.sm.auth;

import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.SMRequestInterface;

public interface SMAuthenticationInterface {

    SMRequestInterface getRequestBody() throws SecureMessengerClientException;

}
