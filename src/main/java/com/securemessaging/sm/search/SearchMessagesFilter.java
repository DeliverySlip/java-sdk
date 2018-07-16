package com.securemessaging.sm.search;

import com.securemessaging.sm.enums.MessageBoxType;

public class SearchMessagesFilter {

    private String searchCriteria = "";
    private String types = "Inbox";
    private int pageSize = 25;
    private int page = 1;

    public void setPageSize(int pageSize){
        this.pageSize = pageSize;
    }

    public int getPageSize(){
        return this.pageSize;
    }

    public int getPageNumber(){
        return this.page;
    }

    public void setPageNumber(int pageNumber){
        this.page = pageNumber;
    }

    public void setSearchCriteria(String searchCriteria){
        this.searchCriteria = searchCriteria;
    }

    public void setMessageBoxType(MessageBoxType messageBoxType){
        this.types = messageBoxType.getEnumText();
    }

    public String getSearchCriteria(){
        return this.searchCriteria;
    }

    public MessageBoxType getMessageBoxType(){
        return MessageBoxType.enumFromEnumText(this.types);
    }
}
