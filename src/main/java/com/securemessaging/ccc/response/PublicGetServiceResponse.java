package com.securemessaging.ccc.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PublicGetServiceResponse {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PublicServiceUrls{
        public String MessagingApi;
        public String Webmail;
        public String ToolbarServices;
        public String AdminConsole;
        public String SecMsgAPI;
        public String Webapp;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PublicServiceBranding{
        public String FavIco;
        public String Square24x;
        public String Square48x;
        public String Square64x;
    }


    public PublicServiceUrls urls;
    public PublicServiceBranding branding;

    public String serviceGuid;
    public String serviceStatus;
    public String serviceName;
    public String serviceCode;
    public String collectionGuid;
    public String primaryDomain;
    public boolean ssoEnabled;


}
