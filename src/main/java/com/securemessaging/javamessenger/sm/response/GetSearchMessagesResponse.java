package com.securemessaging.javamessenger.sm.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSearchMessagesResponse {

    public int pageSize;
    public int totalPages;
    public int totalItems;
    public int currentPage;


    public ArrayList<MessageSummaryResponse> results = new ArrayList<MessageSummaryResponse>();



}
