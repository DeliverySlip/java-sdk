package com.securemessaging.ex;

/**
 * SecureMessengerClientException is the exception handler thrown for all client side related errors. These can
 * include response handling errors or any custom validation errors handled by the client SDK. Note this exception
 * should ONLY be thrown if there is an unexpected issue on the client side. An error returned by the API should
 * throw a SecureMessengerException
 */
public class SecureMessengerClientException extends Exception {

    public SecureMessengerClientException(String errorMessage){
        super(errorMessage);
    }
}
