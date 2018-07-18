import com.securemessaging.SecureMessenger;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.sm.Message;
import com.securemessaging.sm.attachments.AttachmentManager;
import com.securemessaging.sm.attachments.AttachmentPlaceholder;
import com.securemessaging.sm.attachments.AttachmentSummary;
import com.securemessaging.sm.enums.BodyFormat;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.*;
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

            InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
            File tempFile = File.createTempFile("yellow-", ".jpg");
            tempFile.deleteOnExit();
            OutputStream oStream = new FileOutputStream(tempFile);

            int read = 0;
            byte[] bytes = new byte[1024];
            while((read = iStream.read(bytes)) != -1){
                oStream.write(bytes, 0, read);
            }

            oStream.flush();
            oStream.close();

            messenger.uploadAttachmentsForMessage(message, new File[]{tempFile});

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

            InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
            File tempFile = File.createTempFile("yellow-", ".jpg");
            tempFile.deleteOnExit();
            OutputStream oStream = new FileOutputStream(tempFile);

            int read = 0;
            byte[] bytes = new byte[1024];
            while((read = iStream.read(bytes)) != -1){
                oStream.write(bytes, 0, read);
            }

            oStream.flush();
            oStream.close();

            manager.addAttachmentFile(tempFile);
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
    public void testAttachmentManagerUploadAndDownloadEachAttachment() throws SecureMessengerException, SecureMessengerClientException, IOException{

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

            InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
            File tempFile = File.createTempFile("yellow-", ".jpg");
            tempFile.deleteOnExit();
            OutputStream oStream = new FileOutputStream(tempFile);

            int read = 0;
            byte[] bytes = new byte[1024];
            while((read = iStream.read(bytes)) != -1){
                oStream.write(bytes, 0, read);
            }

            oStream.flush();
            oStream.close();


            manager.addAttachmentFile(tempFile);
            manager.preCreateAllAttachments();
            manager.uploadAllAttachments();

            message = messenger.saveMessage(message);
            messenger.sendMessage(message);


            List<AttachmentSummary> attachments = manager.getAttachmentsInfo();

            for(AttachmentSummary attachment : attachments){
                System.out.println("Downloading Attachment: " + attachment.fileName + "." + attachment.originalFileExtension);
                manager.downloadAttachment(attachment, ".");
            }


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
    public void testAttachmentManagerUploadAndDownloadAllAttachments() throws SecureMessengerException, SecureMessengerClientException, IOException{


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

            InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
            File tempFile = File.createTempFile("yellow-", ".jpg");
            tempFile.deleteOnExit();
            OutputStream oStream = new FileOutputStream(tempFile);

            int read = 0;
            byte[] bytes = new byte[1024];
            while((read = iStream.read(bytes)) != -1){
                oStream.write(bytes, 0, read);
            }

            oStream.flush();
            oStream.close();


            manager.addAttachmentFile(tempFile);
            manager.preCreateAllAttachments();
            manager.uploadAllAttachments();

            message = messenger.saveMessage(message);
            messenger.sendMessage(message);


            manager.downloadAllAttachments(".");


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

            AttachmentManager manager = messenger.createAttachmentManagerForMessage(message);

            InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
            File tempFile = File.createTempFile("yellow-", ".jpg");
            tempFile.deleteOnExit();
            OutputStream oStream = new FileOutputStream(tempFile);

            int read = 0;
            byte[] bytes = new byte[1024];
            while((read = iStream.read(bytes)) != -1){
                oStream.write(bytes, 0, read);
            }

            oStream.flush();
            oStream.close();


            manager.addAttachmentFile(tempFile);
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

                InputStream iStream = ClassLoader.getSystemResourceAsStream("yellow.jpg");
                File tempFile = File.createTempFile("yellow-", ".jpg");
                tempFile.deleteOnExit();
                OutputStream oStream = new FileOutputStream(tempFile);

                int read = 0;
                byte[] bytes = new byte[1024];
                while((read = iStream.read(bytes)) != -1){
                    oStream.write(bytes, 0, read);
                }

                oStream.flush();
                oStream.close();

                manager.addAttachmentFile(tempFile);
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
