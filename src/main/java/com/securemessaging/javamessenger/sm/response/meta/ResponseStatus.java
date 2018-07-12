package com.securemessaging.javamessenger.sm.response.meta;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseStatus {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private class ResponseStatusObject{
        public String errorCode;
        public String message;
    }

    private ResponseStatusObject responseStatus;

    public boolean serializationSucceeded(){
        return responseStatus != null && responseStatus.errorCode != null && responseStatus.message != null;
    }

    public String getErrorCode(){
        if(responseStatus == null){
            return "UNKNOWN";
        }else if(responseStatus.errorCode == null){
            return "UNKNOWN";
        }else{
            return responseStatus.errorCode;
        }
    }

    public String getErrorMessage(){
        if(responseStatus == null){
            return "The API Returned No Response Body For Its Error. Cannot Determine Cause Of Error";
        }else if(responseStatus.message == null){
            return "The API Returned No Error Code In The Response Body. Cannot Determine Cause Of Error";
        }else{
            return responseStatus.message;
        }
    }

}
