package com.securemessaging.utils;

import com.securemessaging.Message;
import com.securemessaging.campaign.CampaignMessage;
import com.securemessaging.sm.enums.BodyFormat;
import com.securemessaging.sm.enums.FyeoType;
import com.securemessaging.sm.response.MessageSummaryResponse;

public class SMConverter {

    public static CampaignMessage convertMessageSummaryToCampaignMessage(MessageSummaryResponse messageSummary){

        CampaignMessage message = new CampaignMessage();
        message.setMessageGuid(messageSummary.guid);

        if(messageSummary.bcc != null){
            String[] bccList = new String[messageSummary.bcc.size()];
            for(int i = 0; i < bccList.length; i++){
                bccList[i] = messageSummary.bcc.get(i).email;
            }
            message.setBCC(bccList);
        }

        if(messageSummary.cc != null){
            String[] ccList = new String[messageSummary.cc.size()];
            for(int i = 0; i < ccList.length; i++){
                ccList[i] = messageSummary.cc.get(i).email;
            }
            message.setCC(ccList);
        }

        if(messageSummary.to != null){
            String[] toList = new String[messageSummary.to.size()];
            for(int i = 0; i < toList.length; i++){
                toList[i] = messageSummary.to.get(i).email;
            }
            message.setTo(toList);
        }

        if(messageSummary.sender != null && messageSummary.sender.email != null){
            message.setFrom(new String[]{messageSummary.sender.email});
        }
        if(messageSummary.body != null){
            message.setBody(messageSummary.body);
        }
        if(messageSummary.bodyFormat != null){
            message.setBodyFormat(messageSummary.bodyFormat);
        }
        if(messageSummary.subject != null){
            message.setSubject(messageSummary.subject);
        }
        if(messageSummary.messageOptions != null){
            message.setTracking(messageSummary.messageOptions.allowTracking);
            message.setShareTracking(messageSummary.messageOptions.shareTracking);
            message.setForward(messageSummary.messageOptions.allowForward);
            message.setReply(messageSummary.messageOptions.allowReply);

            if(messageSummary.messageOptions.fyeoType != null){
                message.setFyeoType(messageSummary.messageOptions.fyeoType);
            }
        }

        return message;
    }

    public static Message convertMessageSummaryToMessage(MessageSummaryResponse messageSummary){

        Message message = new Message();
        message.setMessageGuid(messageSummary.guid);

        String[] bccList = new String[messageSummary.bcc.size()];
        for(int i = 0; i < bccList.length; i++){
            bccList[i] = messageSummary.bcc.get(i).email;
        }
        message.setBCC(bccList);

        String[] ccList = new String[messageSummary.cc.size()];
        for(int i = 0; i < ccList.length; i++){
            ccList[i] = messageSummary.cc.get(i).email;
        }
        message.setCC(ccList);

        String[] toList = new String[messageSummary.to.size()];
        for(int i = 0; i < toList.length; i++){
            toList[i] = messageSummary.to.get(i).email;
        }
        message.setTo(toList);

        message.hasBeenSaved = true;
        message.setFrom(new String[]{messageSummary.sender.email});
        message.setBody(messageSummary.body);
        message.setBodyFormat(messageSummary.bodyFormat);
        message.setSubject(messageSummary.subject);
        message.setTracking(messageSummary.messageOptions.allowTracking);
        message.setShareTracking(messageSummary.messageOptions.shareTracking);

        message.setForward(messageSummary.messageOptions.allowForward);
        message.setReply(messageSummary.messageOptions.allowReply);
        message.setFyeoType(messageSummary.messageOptions.fyeoType);


        //message.setPassword(messageSummary);

        return message;
    }
}
