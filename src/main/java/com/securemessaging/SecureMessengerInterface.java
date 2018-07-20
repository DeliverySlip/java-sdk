package com.securemessaging;

import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Message;
import com.securemessaging.sm.PreCreateConfiguration;
import com.securemessaging.sm.attachments.AttachmentManager;
import com.securemessaging.sm.auth.SMAuthenticationInterface;
import com.securemessaging.sm.notifications.NotificationManager;
import com.securemessaging.sm.search.SearchMessagesFilter;
import com.securemessaging.sm.search.SearchMessagesResults;

import java.io.File;
import java.io.IOException;

public interface SecureMessengerInterface {

    void setClientName(String clientName);

    void setClientVersion(String clientVersion);

    String getClientVersion();

    void login(SMAuthenticationInterface authentication) throws SecureMessengerException, SecureMessengerClientException;

    Message preCreateMessage(PreCreateConfiguration preCreateConfiguration) throws SecureMessengerException, SecureMessengerClientException;

    Message preCreateMessage() throws SecureMessengerException, SecureMessengerClientException;

    Message getMessage(String messageGuid) throws SecureMessengerException, SecureMessengerClientException;

    SearchMessagesResults searchMessages(SearchMessagesFilter searchMessagesFilter) throws SecureMessengerException, SecureMessengerClientException;

    NotificationManager getNotificationManagerForSession() throws SecureMessengerException, SecureMessengerClientException;

    SavedMessage saveMessage(Message email) throws SecureMessengerException, SecureMessengerClientException;

    void sendMessage(SavedMessage email) throws SecureMessengerClientException, SecureMessengerException;

    AttachmentManager createAttachmentManagerForMessage(Message message);

    void uploadAttachmentsForMessage(Message message, File[] attachments)
            throws SecureMessengerClientException, SecureMessengerException,
            IOException;

    String getAuthenticationToken(int numberOfDaysUntilExpiry) throws SecureMessengerException, SecureMessengerClientException;

    void deleteAuthenticationToken(String authenticationToken) throws SecureMessengerException, SecureMessengerClientException;

    void deleteAllAuthenticationTokens() throws SecureMessengerException, SecureMessengerClientException;

}
