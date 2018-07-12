import com.securemessaging.javamessenger.SecureMessenger;
import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.ex.SecureMessengerException;
import com.securemessaging.javamessenger.sm.Credentials;
import com.securemessaging.javamessenger.sm.Message;
import com.securemessaging.javamessenger.sm.enums.BodyFormat;
import com.securemessaging.javamessenger.sm.enums.notifications.EventType;
import com.securemessaging.javamessenger.sm.notifications.NotificationManager;
import com.securemessaging.javamessenger.sm.notifications.events.EventDataInterface;
import com.securemessaging.javamessenger.sm.notifications.events.NewAssetPayload;
import com.securemessaging.javamessenger.sm.notifications.events.UserTrackingPayload;
import org.junit.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class NotificationTests extends BaseTestCase {



    @Test
    public void testRegisteringForNotifications() throws SecureMessengerException, SecureMessengerClientException, Exception {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            messenger.saveMessage(message);


            NotificationManager manager = messenger.getNotificationManagerForSession();
            manager.establishConnection();

        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(Exception e){
            Assert.fail();
            throw e;
        }


    }

    @Test
    public void testRegisterAndListenForNotifications() throws SecureMessengerException, SecureMessengerClientException, Exception {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            messenger.saveMessage(message);


            NotificationManager manager = messenger.getNotificationManagerForSession();
            manager.establishConnection();
            manager.startListeningForEvents();

        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(Exception e){
            Assert.fail();
            throw e;
        }


    }

    @Test
    public void testRegisterListenAndDisconnectForNotifications() throws SecureMessengerException, SecureMessengerClientException, Exception {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            messenger.saveMessage(message);


            NotificationManager manager = messenger.getNotificationManagerForSession();
            manager.establishConnection();
            manager.startListeningForEvents();
            manager.disconnectEverything();

        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(Exception e){
            Assert.fail();
            throw e;
        }
    }

    @Test
    @Disabled
    public void testHangingForEvents() throws SecureMessengerException, SecureMessengerClientException, Exception {

        try{
            final SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            final Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            messenger.saveMessage(message);

            try{

                NotificationManager manager = messenger.getNotificationManagerForSession();

                manager.setOnNotificationEventLister(new NotificationManager.OnNotificationEventListener() {
                    @Override
                    public void onConnected() {
                        System.out.println("The NotificationManager Has Connected To The Notification Service!");
                    }

                    @Override
                    public void onDisconnected() {
                        System.out.println("The NotificationManager Has Disconnected From the Notification Service!");
                    }

                    @Override
                    public void onListeningForEvents() {
                        System.out.println("The NotificationManager Has Started Listening For Notifications!");
                    }

                    @Override
                    public void onNotificationEvent(EventType eventType, EventDataInterface eventData) {
                        System.out.println("A Notification Has Arrived!");

                        switch(eventType){
                            case USERTRACKINGPAYLOAD:
                                UserTrackingPayload utp = (UserTrackingPayload)eventData;
                                System.out.println("UserTrackingPayload Cast Successful");
                            case NEWASSETPAYLOAD:
                                NewAssetPayload nap = (NewAssetPayload)eventData;
                                System.out.println("NewAssetPayload Case Successful");
                        }
                    }

                    @Override
                    public void onNotificationEventError(EventType eventType, String eventData, SecureMessengerClientException smce) {
                        System.out.println("An Error Has Occurred Trying To Handle The Incoming Event!");
                        System.out.println("EventType: " + eventType.name() + " EventData: " + eventData);
                        smce.printStackTrace();
                    }
                });



                manager.establishConnection();
                manager.startListeningForEvents();




            }catch(SecureMessengerException sme){
                sme.printStackTrace();
            }catch(SecureMessengerClientException smce){
                smce.printStackTrace();
            }catch(Exception e){
                e.printStackTrace();
            }


            messenger.sendMessage(message);

            while(true){
                Thread.sleep(10 * 1000 * 60);
            }

        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(Exception e){
            Assert.fail();
            throw e;
        }


    }


}
