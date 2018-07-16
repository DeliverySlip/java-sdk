package com.securemessaging.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMessageResponse extends MessageSummaryResponse {

    public GetMessageResponse(){
        super();
    }

}
