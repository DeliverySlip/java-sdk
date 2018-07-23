import com.securemessaging.SavedMessage;
import com.securemessaging.SecureMessenger;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.Message;
import com.securemessaging.sm.PreCreateConfiguration;
import com.securemessaging.sm.enums.ActionCode;
import com.securemessaging.sm.enums.BodyFormat;
import com.securemessaging.sm.enums.FyeoType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SendMessageTests extends BaseTestCase{


    @Test
    public void testSendBasicMessage() throws SecureMessengerException, SecureMessengerClientException {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            SavedMessage savedMessage = messenger.saveMessage(message);
            messenger.sendMessage(savedMessage);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }

    }

    @Test
    public void testSendBasicMessage2() throws SecureMessengerException, SecureMessengerClientException {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            PreCreateConfiguration config = new PreCreateConfiguration();
            config.setActionCode(ActionCode.NEW);
            Message message = messenger.preCreateMessage(config);

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            SavedMessage savedMessage = messenger.saveMessage(message);
            messenger.sendMessage(savedMessage);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }

    }

    @Test
    public void testSendFYEOMessageAccountPassword() throws SecureMessengerClientException, SecureMessengerException {


        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message.setFyeoType(FyeoType.ACCOUNTPASSWORD);
            message.setPassword(password);

            SavedMessage savedMessage = messenger.saveMessage(message);
            messenger.sendMessage(savedMessage);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }

    }

    @Test
    public void testSendMessageWithCRA()  throws SecureMessengerClientException, SecureMessengerException{

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message.setCraCode("cracode");
            message.setInviteNewUsers(true);
            message.setSendNotification(true);

            SavedMessage savedMessage = messenger.saveMessage(message);
            messenger.sendMessage(savedMessage);

        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }


    }

    @Test
    public void testSendFYEOMessageUniquePassword() throws SecureMessengerClientException, SecureMessengerException {


        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message.setFyeoType(FyeoType.UNIQUEPASSWORD);
            message.setPassword("password");

            SavedMessage savedMessage = messenger.saveMessage(message);
            messenger.sendMessage(savedMessage);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }


    }


}
