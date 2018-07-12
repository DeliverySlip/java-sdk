package com.securemessaging.javamessenger.sm.auth;

import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.SMRequestInterface;

public interface SMAuthenticationInterface {

    SMRequestInterface getRequestBody() throws SecureMessengerClientException;

}
