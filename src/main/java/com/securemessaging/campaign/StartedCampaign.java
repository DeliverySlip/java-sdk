package com.securemessaging.campaign;

import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Session;
import com.securemessaging.campaign.Campaign;
import com.securemessaging.sm.request.PostStopCampaignRequest;

public class StartedCampaign {

    Campaign campaign;
    Session session;

    public Campaign stopCampaign() throws SecureMessengerException, SecureMessengerClientException {

        PostStopCampaignRequest request = new PostStopCampaignRequest();
        request.campaignGuid = campaign.getCampaignGuid();

        session.client.makeRequest(request.getRequestRoute(), request, String.class);

        return campaign;

    }
}
