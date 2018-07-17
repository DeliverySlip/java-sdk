package com.securemessaging.sm.notifications;

import com.securemessaging.ex.SecureMessengerClientException;

public interface NotificationManagerInterface {

    void setOnNotificationEventLister(NotificationManager.OnNotificationEventListener listener);

    void startHeartbeatThread();

    void stopHeartbeatThread();

    void establishConnection() throws InterruptedException, UnsupportedOperationException, SecureMessengerClientException;

    void startListeningForEvents() throws InterruptedException, UnsupportedOperationException, SecureMessengerClientException;

    boolean isConnected();

    boolean isDisconnected();

    void disconnectEverything() throws InterruptedException;
}
