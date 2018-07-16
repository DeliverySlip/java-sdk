package com.securemessaging.sm.notifications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.ChannelEventListener;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;
import com.securemessaging.SecureMessenger;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.sm.enums.notifications.EventType;
import com.securemessaging.sm.enums.notifications.NotificationType;
import com.securemessaging.sm.notifications.events.EventDataInterface;
import com.securemessaging.sm.notifications.events.NewAssetPayload;
import com.securemessaging.sm.notifications.events.UserTrackingPayload;
import com.securemessaging.sm.request.PostSubscribeRequest;
import com.securemessaging.sm.request.PutUpdateNotificationHeartbeatRequest;
import com.securemessaging.sm.response.PutUpdateNotificationHeartbeatResponse;
import com.securemessaging.sm.response.meta.ResponseStatus;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class NotificationManager {

    public interface OnNotificationEventListener{

        void onConnected();

        void onDisconnected();

        void onListeningForEvents();

        void onNotificationEvent(EventType eventType, EventDataInterface eventData);

        void onNotificationEventError(EventType eventType, String eventData, SecureMessengerClientException smce);
    }

    private Pusher pusher;
    private String notificationSessionToken;
    private Channel channel;
    //private List<PostSubscribeRequest.Subscription> subscriptions;

    private CountDownLatch disconnectEverythingLatch = new CountDownLatch(1);

    private boolean isDisconnected = false;

    final private ClientRequestHandler client;

    private HeartbeatThread heartbeartThread;

    private OnNotificationEventListener onNotificationEventListener = null;
    public void setOnNotificationEventLister(OnNotificationEventListener listener){
        onNotificationEventListener = listener;
    }



    public NotificationManager(String notificationSessionToken, String serviceApiBaseUrl){

        PusherOptions options = new PusherOptions();
        options.setCluster("mt1");
        this.pusher = new Pusher("637003f8185c4a167178", options);

        this.notificationSessionToken = notificationSessionToken;
        //this.subscriptions = subscriptions;
        this.isDisconnected = false;

        this.client = new ClientRequestHandler(serviceApiBaseUrl);

    }


    public void startHeartbeatThread(){
        this.heartbeartThread = new HeartbeatThread(this.client);
        this.heartbeartThread.start();
    }

    public void stopHeartbeatThread(){
        this.heartbeartThread.terminate();
        this.heartbeartThread.interrupt();
    }


    private class HeartbeatThread extends Thread{

        private ClientRequestHandler client;

        private boolean keepRunning = true;

        HeartbeatThread(ClientRequestHandler client){
            this.client = client;
        }

        @Override
        public void run(){

            while(keepRunning){

                try{
                    //now call the update
                    PutUpdateNotificationHeartbeatRequest request = new PutUpdateNotificationHeartbeatRequest();
                    request.notificationSessionToken = notificationSessionToken;

                    PutUpdateNotificationHeartbeatResponse response =  this.client.makeRequest(request.getRequestRoute(), request, PutUpdateNotificationHeartbeatResponse.class);

                    Thread.sleep(25 * 60 * 1000);
                }catch(Exception e){
                    //this is just a general ping we can let it keep running
                }
            }
        }

        void terminate(){
            keepRunning = false;
        }
    }

    public void establishConnection() throws InterruptedException, UnsupportedOperationException, SecureMessengerClientException{

        final SecureMessengerClientException[] exception = {null};

        if(isDisconnected){
            throw new UnsupportedOperationException("NotificationManager was Disconnected. It Can't Be Used Anymore." +
                    "Please Generate Another NotificationManager from the SecureMessenger");
        }

        final CountDownLatch establishConnectionLatch = new CountDownLatch(1);
        final CountDownLatch subscribeToChannelLatch = new CountDownLatch(1);

        this.pusher.connect(new com.pusher.client.connection.ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {

                //System.out.println("State changed to " + change.getCurrentState() + " from " + change.getPreviousState());

                    switch (change.getCurrentState()){
                        case CONNECTED:

                            if(establishConnectionLatch.getCount() > 0){
                                establishConnectionLatch.countDown();
                            }

                            if(onNotificationEventListener != null){
                                onNotificationEventListener.onConnected();
                            }

                            break;

                        case CONNECTING:

                            break;

                        case DISCONNECTED:

                            if(isDisconnected){
                                disconnectEverythingLatch.countDown();
                            }

                            if(onNotificationEventListener != null){
                                onNotificationEventListener.onDisconnected();
                            }

                            break;

                        case DISCONNECTING:


                            isDisconnected = true;

                            break;

                        case RECONNECTING:

                            break;

                        case ALL:

                            break;

                        default:

                    }


            }

            @Override
            public void onError(String message, String code, Exception e) {

                exception[0] = new SecureMessengerClientException("Message: " + message + " (ErrorCode: " + code
                        + "). ExceptionMessage: " + e.getMessage());
                establishConnectionLatch.countDown();
            }
        }, ConnectionState.ALL);

        establishConnectionLatch.await();

        //if connection had an exception - throw it
        if(exception[0] != null){
            throw exception[0];
        }


        this.channel = this.pusher.subscribe(this.notificationSessionToken, new ChannelEventListener() {
            @Override
            public void onSubscriptionSucceeded(String channelName) {
                if(subscribeToChannelLatch.getCount() > 0){
                    subscribeToChannelLatch.countDown();
                }

                if(onNotificationEventListener != null){
                    onNotificationEventListener.onListeningForEvents();
                }

            }

            @Override
            public void onEvent(String channelName, String eventName, String data) {

            }
        });

        subscribeToChannelLatch.await();

    }

    public void startListeningForEvents() throws InterruptedException, UnsupportedOperationException, SecureMessengerClientException{
        if(isDisconnected){
            throw new UnsupportedOperationException("NotificationManager was Disconnected. It Can't Be Used Anymore." +
                    "Please Generate Another NotificationManager from the SecureMessenger");
        }

        final SecureMessengerClientException[] exception = new SecureMessengerClientException[1];

        for(final EventType eventType: EventType.values()){
            this.channel.bind(eventType.getEnumText(), new SubscriptionEventListener() {
                @Override
                public void onEvent(String channelName, String eventName, String data) {

                    if(EventType.isValidEventTypeText(eventName)){
                        EventType eventType = EventType.enumFromEnumText(eventName);

                        ObjectMapper mapper = new ObjectMapper();

                        switch(eventType){
                            case NEWASSETPAYLOAD:
                                try{
                                    NewAssetPayload eventData = mapper.readValue(data, NewAssetPayload.class);

                                    if(onNotificationEventListener != null){
                                        onNotificationEventListener.onNotificationEvent(eventType, eventData);
                                    }

                                }catch(IOException ioe){
                                    if(onNotificationEventListener != null){
                                        onNotificationEventListener.onNotificationEventError(eventType, data, new SecureMessengerClientException(ioe.getMessage()));
                                    }
                                }
                                break;


                            case USERTRACKINGPAYLOAD:

                                try{
                                    UserTrackingPayload eventData = mapper.readValue(data, UserTrackingPayload.class);

                                    if(onNotificationEventListener != null){
                                        onNotificationEventListener.onNotificationEvent(eventType, eventData);
                                    }

                                }catch(IOException ioe){
                                    if(onNotificationEventListener != null){
                                        onNotificationEventListener.onNotificationEventError(eventType, data, new SecureMessengerClientException(ioe.getMessage()));
                                    }
                                }
                                break;

                            default:
                                if(onNotificationEventListener != null){
                                    onNotificationEventListener.onNotificationEventError(eventType, data, new SecureMessengerClientException(
                                            "A Known Event Type Was Detected But Has No Serialization Handler. Can't Process Event"
                                    ));
                                }
                        }

                    }else{
                        if(onNotificationEventListener != null){
                            onNotificationEventListener.onNotificationEventError(null, data, new SecureMessengerClientException(
                                    "An Unknown Event Type Was Detected. Can't Process Event ( EventName: "
                                            + eventName +", EventType: " + eventType + " )"
                            ));
                        }
                    }


                }
            });
        }

    }

    public boolean isConnected(){
        return !this.isDisconnected;
    }

    public boolean isDisconnected(){
        return this.isDisconnected;
    }

    public void disconnectEverything() throws InterruptedException{

        //this will have some issues if client is multi-threading
        disconnectEverythingLatch = new CountDownLatch(1);

        this.pusher.disconnect();

        this.isDisconnected = true;

        disconnectEverythingLatch.await();
    }








}
