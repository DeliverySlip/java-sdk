import com.securemessaging.AttachmentManager;
import com.securemessaging.campaign.Campaign;
import com.securemessaging.campaign.CampaignManager;
import com.securemessaging.SecureMessenger;
import com.securemessaging.campaign.CampaignMessage;
import com.securemessaging.campaign.StartedCampaign;
import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Credentials;
import com.securemessaging.sm.Session;
import com.securemessaging.sm.auth.ServiceCodeResolver;
import com.securemessaging.sm.enums.CampaignMode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CampaignTests extends BaseTestCase {

    @Test
    public void testCreateCampaign() throws SecureMessengerException, SecureMessengerClientException {

        String secureMessagingApi = ServiceCodeResolver.resolve(serviceCode);
        ClientRequestHandler client = new ClientRequestHandler(secureMessagingApi);

        SecureMessenger messenger = new SecureMessenger(client);
        Credentials credentials = new Credentials(username, password);

        Session session = messenger.login(credentials);
        CampaignManager manager = new CampaignManager(session);
    }

    @Test
    public void testStartCampaignFewLinesPossible() throws SecureMessengerException, SecureMessengerClientException {

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        Session session = messenger.login(credentials);

        CampaignManager campaignManager = new CampaignManager(session);
        Campaign campaign = campaignManager.createNewCampaign("Test Campaign", "Description", CampaignMode.AUTOMATIC);

        List<String> campaignRecipients = new ArrayList<String>();
        campaignRecipients.add(recipientEmail);

        campaignManager.addCampaignRecipients(campaign, campaignRecipients);
        campaignManager.recallSentMessagesWhenRecipientsAreRemovedFromCampaign(true, "This Message " +
                "Has Been Recalled Because You Were Removed From The Recipients List Of The Campaign");

        //get the template message that everyone in the campaign will receive
        CampaignMessage message = campaignManager.getCampaignMessageTemplate(campaign);
        message.setSubject("TEMPLATE CAMPAIGN SUBJECT");
        message.setBody("TEMPLATE CAMPAIGN BODY");

        campaignManager.saveCampaignMessageTemplate(message);
        campaignManager.startCampaign(campaign);
    }

    @Test
    public void testStartAndStopCampaign() throws SecureMessengerException, SecureMessengerClientException {

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        Session session = messenger.login(credentials);

        CampaignManager campaignManager = new CampaignManager(session);
        Campaign campaign = campaignManager.createNewCampaign("Test Campaign", "Description", CampaignMode.AUTOMATIC);

        List<String> campaignRecipients = new ArrayList<String>();
        campaignRecipients.add(recipientEmail);

        campaignManager.addCampaignRecipients(campaign, campaignRecipients);
        campaignManager.recallSentMessagesWhenRecipientsAreRemovedFromCampaign(true, "This Message " +
                "Has Been Recalled Because You Were Removed From The Recipients List Of The Campaign");

        //get the template message that everyone in the campaign will receive
        CampaignMessage message = campaignManager.getCampaignMessageTemplate(campaign);
        message.setSubject("TEMPLATE CAMPAIGN SUBJECT");
        message.setBody("TEMPLATE CAMPAIGN BODY");

        campaignManager.saveCampaignMessageTemplate(message);
        StartedCampaign startedCampaign = campaignManager.startCampaign(campaign);
        campaign = startedCampaign.stopCampaign();
    }

    @Test
    public void testStartCampaignWithAttachments() throws SecureMessengerException, SecureMessengerClientException, IOException, URISyntaxException {

        SecureMessenger messenger = SecureMessenger.resolveViaServiceCode(serviceCode);
        Credentials credentials = new Credentials(username, password);
        Session session = messenger.login(credentials);

        CampaignManager campaignManager = new CampaignManager(session);
        Campaign campaign = campaignManager.createNewCampaign("Test Campaign", "Description", CampaignMode.AUTOMATIC);

        List<String> campaignRecipients = new ArrayList<String>();
        campaignRecipients.add(recipientEmail);

        campaignManager.addCampaignRecipients(campaign, campaignRecipients);
        campaignManager.recallSentMessagesWhenRecipientsAreRemovedFromCampaign(true, "This Message " +
                "Has Been Recalled Because You Were Removed From The Recipients List Of The Campaign");

        //get the template message that everyone in the campaign will receive
        CampaignMessage message = campaignManager.getCampaignMessageTemplate(campaign);
        message.setSubject("TEMPLATE CAMPAIGN SUBJECT");
        message.setBody("TEMPLATE CAMPAIGN BODY");

        //save the template
        campaignManager.saveCampaignMessageTemplate(message);

        //A CampaignMessage IS-A Message - so we can add attachments to it like normal messages
        AttachmentManager aManager = messenger.createAttachmentManagerForMessage(message);
        URL resource = ClassLoader.getSystemResource("yellow.jpg");
        File file = new File(resource.toURI());
        FileInputStream fileInputStream = new FileInputStream(file);

        Assert.assertTrue("Attachment Successfully Added", aManager.addAttachmentFile(fileInputStream, "yellow.jpg"));
        aManager.preCreateAllAttachments();
        aManager.uploadAllAttachments();

        campaignManager.startCampaign(campaign);
    }
}
