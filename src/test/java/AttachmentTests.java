import com.securemessaging.javamessenger.SecureMessenger;
import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.ex.SecureMessengerException;
import com.securemessaging.javamessenger.sm.Credentials;
import com.securemessaging.javamessenger.sm.Message;
import com.securemessaging.javamessenger.sm.attachments.AttachmentManager;
import com.securemessaging.javamessenger.sm.attachments.AttachmentPlaceholder;
import com.securemessaging.javamessenger.sm.enums.BodyFormat;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class AttachmentTests extends BaseTestCase {

    @Test
    public void testSendAttachmentMessengerWorkflow()throws SecureMessengerException, SecureMessengerClientException, IOException {

        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message = messenger.saveMessage(message);

            File file = new File("resources/yellow.jpg");
            messenger.uploadAttachmentsForMessage(message, new File[]{file});

            message = messenger.saveMessage(message);
            messenger.sendMessage(message);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }
    }

    @Test
    public void testSendAttachmentManagerWorkflow() throws SecureMessengerException, SecureMessengerClientException, IOException{


        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message = messenger.saveMessage(message);

            AttachmentManager manager = messenger.createAttachmentManagerForMessage(message);
            manager.addAttachmentFile(new File("resources/yellow.jpg"));
            manager.preCreateAllAttachments();
            manager.uploadAllAttachments();

            message = messenger.saveMessage(message);
            messenger.sendMessage(message);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(IOException ioe){
            Assert.fail();
            throw ioe;
        }

    }

    @Test
    public void testSendAttachmentManagerListenerWorkflow() throws SecureMessengerException, SecureMessengerClientException, IOException{


        try{
            SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
            Credentials credentials = new Credentials(username, password);
            messenger.login(credentials);

            Message message = messenger.preCreateMessage();

            message.setTo(new String[]{recipientEmail});
            message.setSubject("DeliverySlip Java Example");

            message.setBody("Hello Test Message From DeliverySlip Java Example");
            message.setBodyFormat(BodyFormat.TEXT);

            message = messenger.saveMessage(message);

            File file = new File("resources/yellow.jpg");
            AttachmentManager manager = messenger.createAttachmentManagerForMessage(message);
            manager.addAttachmentFile(file);
            manager.preCreateAllAttachments();

            //user can now get micro progress of each attachment as they are uploaded chunk by chunk
            //onStarted - Called when attachment is selected by sdk for upload
            //onProgress - Called when progress info about the started attachment is available and after each chunk is uploaded there after
            //onComplete - Called when upload of attachment has finished
            manager.setOnAttachmentUploadProgressListener(new AttachmentManager.OnAttachmentUploadEventListenerInterface() {
                @Override
                public void onProgress(AttachmentPlaceholder placeholder, int totalChunks, int chunksUploaded) {

                }

                @Override
                public void onStarted(AttachmentPlaceholder placeholder) {

                }

                @Override
                public void onComplete(AttachmentPlaceholder placeholder) {

                }
            });

            manager.uploadAllAttachments();

            message = messenger.saveMessage(message);
            messenger.sendMessage(message);
        }catch(SecureMessengerException sme){
            Assert.fail();
            throw sme;
        }catch(SecureMessengerClientException smce){
            Assert.fail();
            throw smce;
        }catch(IOException ioe){
            Assert.fail();
            throw ioe;
        }

    }


        @Test
        public void testSendAttachmentManagerIteratorWorkflow() throws SecureMessengerException, SecureMessengerClientException, IOException{


            try{
                SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
                Credentials credentials = new Credentials(username, password);
                messenger.login(credentials);

                Message message = messenger.preCreateMessage();

                message.setTo(new String[]{recipientEmail});
                message.setSubject("DeliverySlip Java Example");

                message.setBody("Hello Test Message From DeliverySlip Java Example");
                message.setBodyFormat(BodyFormat.TEXT);

                message = messenger.saveMessage(message);

                AttachmentManager manager = messenger.createAttachmentManagerForMessage(message);
                manager.addAttachmentFile(new File("resources/yellow.jpg"));
                manager.preCreateAllAttachments();

                //user can now iterate through attachments and return status to the user as each attachment is uploaded
                List<AttachmentPlaceholder> preCreatedAttachments = manager.getAllPreCreatedAttachments();
                Iterator<AttachmentPlaceholder> iterator = preCreatedAttachments.iterator();
                while(iterator.hasNext()){
                    AttachmentPlaceholder placeholder = iterator.next();

                    manager.uploadAttachment(placeholder);
                }

                message = messenger.saveMessage(message);
                messenger.sendMessage(message);
            }catch(SecureMessengerException sme){
                Assert.fail();
                throw sme;
            }catch(SecureMessengerClientException smce){
                Assert.fail();
                throw smce;
            }catch(IOException ioe){
                Assert.fail();
                throw ioe;
            }
        }
}
