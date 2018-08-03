package com.securemessaging.campaign;

import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Session;
import com.securemessaging.sm.enums.CampaignMode;
import com.securemessaging.sm.enums.CampaignStatus;
import com.securemessaging.sm.request.*;
import com.securemessaging.sm.response.GetMessageResponse;
import com.securemessaging.sm.response.GetSearchCampaignsResponse;
import com.securemessaging.sm.response.PostCreateCampaignResponse;
import com.securemessaging.sm.response.PutSaveMessageResponse;
import com.securemessaging.utils.SMConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CampaignManager {

    private Session session;

    private boolean recallMessages = false;
    private String recallMessage = "";

    private HashMap<String, ArrayList<String>> campaignGuid2CampaignRecipients = new HashMap<String, ArrayList<String>>();

    private boolean campaignHasBeenSaved = false;

    public CampaignManager(Session session){
        this.session = session;
    }

    public Campaign createNewCampaign(String campaignName, String campaignDescription, CampaignMode mode) throws SecureMessengerException, SecureMessengerClientException {

        PostCreateCampaignRequest createCampaignRequest = new PostCreateCampaignRequest();
        createCampaignRequest.name = campaignName;
        createCampaignRequest.description = campaignDescription;
        createCampaignRequest.mode = mode.getEnumText();

        PostCreateCampaignResponse createCampaignResponse = session.client.makeRequest(createCampaignRequest.getRequestRoute(), createCampaignRequest, PostCreateCampaignResponse.class);

        ArrayList<String> campaignGuids = new ArrayList<String>();
        campaignGuids.add(createCampaignResponse.campaignGuid);

        GetSearchCampaignsRequest request = new GetSearchCampaignsRequest();
        request.campaignGuids = campaignGuids;
        request.status = CampaignStatus.NOTSTARTED.getEnumText();
        request.searchCriteria = campaignName;

        GetSearchCampaignsResponse response = session.client.makeRequest(request.getRequestRoute(), request, GetSearchCampaignsResponse.class);
        Campaign campaign = response.results.get(0);

        return campaign;
    }


    public void recallSentMessagesWhenRecipientsAreRemovedFromCampaign(boolean recallMessages) {
        recallSentMessagesWhenRecipientsAreRemovedFromCampaign(recallMessages, "");
    }

    //TODO: NEEDS UNIT TESTS CREATED BEFORE MADE PUBLIC
    /*public SearchCampaignsResults searchCampaigns(SearchCampaignsFilter filter) throws SecureMessengerException, SecureMessengerClientException {

        GetSearchCampaignsRequest request = new GetSearchCampaignsRequest();
        request.campaignGuids = filter.getCampaignGuids();
        request.status = filter.getCampaignStatus().getEnumText();
        request.searchCriteria = filter.getSearchCriteria();

        GetSearchCampaignsResponse response = session.client.makeRequest(request.getRequestRoute(), request, GetSearchCampaignsResponse.class);

        return new SearchCampaignsResults(response, filter, this.session.client);
    }*/

    public void recallSentMessagesWhenRecipientsAreRemovedFromCampaign(boolean recallMessages, String recallMessage){
        this.recallMessages = recallMessages;
        this.recallMessage = recallMessage;
    }

    public CampaignMessage getCampaignMessageTemplate(Campaign campaign) throws SecureMessengerException, SecureMessengerClientException {
        GetMessageRequest request = new GetMessageRequest();
        request.messageGuid = campaign.getTemplateMessageGuid();

        GetMessageResponse response = this.session.client.makeRequest(request.getRequestRoute(), request, GetMessageResponse.class);

        return SMConverter.convertMessageSummaryToCampaignMessage(response);
    }

    public void saveCampaignMessageTemplate(CampaignMessage campaignTemplateMessage) throws SecureMessengerException, SecureMessengerClientException {

        PutSaveMessageRequest request = new PutSaveMessageRequest();
        request.messageGuid = campaignTemplateMessage.getMessageGuid();
        request.to = campaignTemplateMessage.getTo();
        request.cc = campaignTemplateMessage.getCC();
        request.bcc = campaignTemplateMessage.getBCC();
        request.subject = campaignTemplateMessage.getSubject();
        request.body = campaignTemplateMessage.getBody();
        request.setBodyFormat(campaignTemplateMessage.getBodyFormat());

        request.messageOptions.setFYEOType(campaignTemplateMessage.getFyeoType());
        request.messageOptions.allowForward = campaignTemplateMessage.getForwardStatus();
        request.messageOptions.allowReply = campaignTemplateMessage.getReplyStatus();
        request.messageOptions.allowTracking = campaignTemplateMessage.getTrackingStatus();
        request.messageOptions.shareTracking = campaignTemplateMessage.getShareTrackingStatus();

        PutSaveMessageResponse response = this.session.client.makeRequest(request.getRequestRoute(), request, PutSaveMessageResponse.class);

        campaignTemplateMessage.hasBeenSaved = true;

        campaignHasBeenSaved = true;
    }

    public List<String> getLocallyKnownCampaignRecipients(Campaign campaign){
        if(this.campaignGuid2CampaignRecipients.containsKey(campaign.getCampaignGuid())){
            return this.campaignGuid2CampaignRecipients.get(campaign.getCampaignGuid());
        }else{
            return new ArrayList<String>();
        }
    }

    public void addCampaignRecipients(Campaign campaign, List<String> emailAddresses) throws SecureMessengerException, SecureMessengerClientException {
        ArrayList<String> newAddresses = new ArrayList<String>();
        for(String emailAddress: emailAddresses){

            if(this.campaignGuid2CampaignRecipients.containsKey(campaign.getCampaignGuid())){
                ArrayList<String> campaignRecipients = this.campaignGuid2CampaignRecipients.get(campaign.getCampaignGuid());

                if(!campaignRecipients.contains(emailAddress)){
                    campaignRecipients.add(emailAddress);
                    this.campaignGuid2CampaignRecipients.put(campaign.getCampaignGuid(), campaignRecipients);

                    newAddresses.add(emailAddress);
                }

            }

        }

        syncAddedCampaignRecipients(campaign, newAddresses);
    }

    public void removeCampaignRecipients(Campaign campaign, List<String> emailAddresses) throws SecureMessengerException, SecureMessengerClientException {
        ArrayList<String> removedAddresses = new ArrayList<String>();
        for(String emailAddress: emailAddresses){

            if(this.campaignGuid2CampaignRecipients.containsKey(campaign.getCampaignGuid())) {
                ArrayList<String> campaignRecipients = this.campaignGuid2CampaignRecipients.get(campaign.getCampaignGuid());

                if(campaignRecipients.contains(emailAddress)){
                    campaignRecipients.remove(emailAddress);
                    this.campaignGuid2CampaignRecipients.put(campaign.getCampaignGuid(), campaignRecipients);

                    removedAddresses.add(emailAddress);
                }
            }

        }

        syncRemovedCampaignRecipients(campaign, removedAddresses, false, "");
    }

    public StartedCampaign startCampaign(Campaign campaign) throws SecureMessengerException, SecureMessengerClientException {

        if(!campaignHasBeenSaved){
            throw new SecureMessengerClientException("The Campaign Template Message Has Not Been Saved Yet. Template Must Be Saved" +
                    " Before Campaign Can Begin");
        }

        PostStartCampaignRequest request = new PostStartCampaignRequest();
        request.campaignGuid = campaign.getCampaignGuid();

        this.session.client.makeRequest(request.getRequestRoute(), request, String.class);

        StartedCampaign startedCampaign = new StartedCampaign();
        startedCampaign.campaign = campaign;
        startedCampaign.session = session;
        return startedCampaign;
    }

    void syncRemovedCampaignRecipients(Campaign campaign, ArrayList<String> removedAddresses, boolean recallMessages,
                                               String recallMessage) throws SecureMessengerException, SecureMessengerClientException {

        DeleteRemoveCampaignRecipientsRequest request = new DeleteRemoveCampaignRecipientsRequest();
        request.campaignGuid = campaign.getCampaignGuid();
        request.emailAddresses = removedAddresses;
        request.recallMessage = recallMessage;
        request.recallMessagesSent = recallMessages;

        this.session.client.makeRequest(request.getRequestRoute(), request, String.class);
    }


    void syncAddedCampaignRecipients(Campaign campaign, ArrayList<String> newAddresses) throws SecureMessengerException, SecureMessengerClientException {

        //API can only take max of 500 per call, so split up our list into arrays of 500
        ArrayList<String[]> choppedParts = new ArrayList<String[]>();

        int index = 1;
        while((index * 500) < newAddresses.size()){
            String[] segment = (String[])newAddresses.subList((index - 1)*500, index*500).toArray();
            choppedParts.add(segment);
        }

        //add remaining parts
        String[] lastSegment = new String[newAddresses.size() - ((index-1)*500)];
        lastSegment = newAddresses.subList((index-1)*500, newAddresses.size()).toArray(lastSegment);
        if(lastSegment.length > 0){
            choppedParts.add(lastSegment);
        }

        //now make a request for each section
        for(String[] recipients: choppedParts){
            PostAddCampaignRecipientsRequest request = new PostAddCampaignRecipientsRequest();
            request.campaignGuid = campaign.getCampaignGuid();
            request.emailAddresses = recipients;

            this.session.client.makeRequest(request.getRequestRoute(), request, String.class);
        }
    }




}
