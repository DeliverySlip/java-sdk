package com.securemessaging.campaign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.securemessaging.sm.enums.CampaignMode;
import com.securemessaging.sm.enums.CampaignStatus;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign {


    private String name;
    private String guid;
    private String description;
    private CampaignMode mode;
    private String created;

    private CampaignStatus status = null;

    private String templateMessageGuid = null;
    private List<TemplateAttachmentGroups> templateAttachmentGroups = null;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TemplateAttachmentGroups{

        public String attachmentGroupGuid = null;
        public String templateAttachmentGuid = null;
        public String name = null;

    }

    @JsonProperty("guid")
    public void setCampaignGuid(String campaignGuid){
        this.guid = campaignGuid;
    }

    @JsonProperty("guid")
    public String getCampaignGuid(){
        return this.guid;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    @JsonProperty("mode")
    public void setCampaignMode(CampaignMode mode){
        this.mode = mode;
    }

    @JsonProperty("mode")
    public CampaignMode getCampaignMode(){
        return this.mode;
    }

    public void setCreated(String created){
        this.created = created;
    }

    public String getCreated(){
        return this.created;
    }

    public void setStatus(CampaignStatus status){
        this.status = status;
    }

    public CampaignStatus getStatus(){
        return this.status;
    }

    public void setTemplateMessageGuid(String templateMessageGuid){
        this.templateMessageGuid = templateMessageGuid;
    }

    public String getTemplateMessageGuid(){
        return this.templateMessageGuid;
    }

    public void setTemplateAttachmentGroups(List<TemplateAttachmentGroups> templateAttachmentGroups){
        this.templateAttachmentGroups = templateAttachmentGroups;
    }

    public List<TemplateAttachmentGroups> getTemplateAttachmentGroups(){
        return this.templateAttachmentGroups;
    }


}
