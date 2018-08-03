package com.securemessaging.campaign;

import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.request.GetSearchCampaignsRequest;
import com.securemessaging.sm.response.GetSearchCampaignsResponse;

import java.util.*;

public class SearchCampaignsResults implements Collection<Campaign>, Iterable<Campaign> {


    private GetSearchCampaignsResponse initialData;
    private SearchCampaignsFilter filter;
    private ClientRequestHandler client;

    private int currentPage;
    private int totalPages;
    private int pageSize;
    private int totalItems;

    private List<Campaign> localCampaignStore = new ArrayList<Campaign>();

    public SearchCampaignsResults(GetSearchCampaignsResponse initialData, SearchCampaignsFilter filter, ClientRequestHandler client){
        this.initialData = initialData;
        this.filter = filter;
        this.client = client;

        this.currentPage = initialData.currentPage;
        this.totalPages = initialData.totalPages;
        this.pageSize = initialData.pageSize;
        this.totalItems = initialData.totalItems;
    }

    public void fetchAllSearchResultsLocally(){
        Iterator<Campaign> iterator = iterator();
        while(iterator.hasNext()){
            iterator.next();
        }
    }

    private void loadNextPageOfDataIntoLocalList(){
        //refill the buffer with the next page
        GetSearchCampaignsRequest request = new GetSearchCampaignsRequest();
        request.searchCriteria = filter.getSearchCriteria();
        request.status = filter.getCampaignStatus().getEnumText();
        request.campaignGuids = filter.getCampaignGuids();

        request.page = ++currentPage;
        request.pageSize = pageSize;

        try{
            GetSearchCampaignsResponse response = client.makeRequest(request.getRequestRoute(), request, GetSearchCampaignsResponse.class);
            currentPage = response.currentPage;
            totalItems = response.totalItems;
            totalPages = response.totalPages;

            localCampaignStore.addAll(response.results);

        }catch(SecureMessengerClientException smce){

            NoSuchElementException nsee = new NoSuchElementException();
            nsee.setStackTrace(smce.getStackTrace());
            throw nsee;

        }catch(SecureMessengerException sme){

            NoSuchElementException nsee = new NoSuchElementException();
            nsee.setStackTrace(sme.getStackTrace());
            throw nsee;

        }
    }


    @Override
    public int size() {
        return this.initialData.totalItems;
    }

    @Override
    public boolean isEmpty() {
        return localCampaignStore.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if(o instanceof Campaign){
            Campaign param = (Campaign) o;

            //try it with what we have to save some time
            for(Campaign campaign: this.localCampaignStore){
                if(param.getCampaignGuid().equals(campaign.getCampaignGuid())){
                    return true;
                }
            }

            // fetch everything and try again
            fetchAllSearchResultsLocally();
            for(Campaign campaign: this.localCampaignStore){
                if(campaign.getCampaignGuid().equals(param.getCampaignGuid())){
                    return true;
                }
            }

            return false;

        }else{
            throw new ClassCastException("Campaign Type Is Only Supported Object");
        }
    }

    @Override
    public Iterator<Campaign> iterator() {
        return new SearchCampaignResultsIterator();
    }

    @Override
    public Object[] toArray() {
        fetchAllSearchResultsLocally();
        return localCampaignStore.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        fetchAllSearchResultsLocally();
        return localCampaignStore.toArray(a);
    }

    @Override
    public boolean add(Campaign campaign) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        if(o instanceof Campaign){

            Campaign param = (Campaign)o;

            //try what we have currently if it exists
            if(this.contains(o)){

                for(Campaign campaign: this.localCampaignStore){
                    if(param.getCampaignGuid().equals(campaign.getCampaignGuid())){
                        return this.localCampaignStore.remove(campaign);
                    }
                }

            }else{
                //fetch everything and try again
                fetchAllSearchResultsLocally();

                for(Campaign campaign: this.localCampaignStore){
                    if(param.getCampaignGuid().equals(campaign.getCampaignGuid())){
                        return this.localCampaignStore.remove(campaign);
                    }
                }
            }

            return false;

        }else{
            throw new ClassCastException("Campaign Type Is Only Supported Object");
        }
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        Iterator<?> iterator = c.iterator();

        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Campaign){
                if(!this.contains(next)){ //contains will ensure we check everything
                    return false;
                }
            }else{
                throw new ClassCastException("Campaign Type Is Only Supported Object");
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Campaign> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Iterator<?> iterator = c.iterator();

        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Campaign){
                if(!this.remove(next)){ //contains will ensure we check everything
                    return false;
                }
            }else{
                throw new ClassCastException("Campaign Type Is Only Supported Object");
            }
        }

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Iterator<Campaign> iterator = iterator();

        while(iterator.hasNext()){
            Campaign campaign = iterator.next();

            //if the passed in collection doesn't have it then remove it
            if(!c.contains(campaign)){

                //if removing fails though we have to return false that this failed
                if(!this.remove(campaign)){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void clear() {
        currentPage = 0;
        this.localCampaignStore.clear();

        loadNextPageOfDataIntoLocalList(); // grab the first page so that our meta is corrected
    }


    private class SearchCampaignResultsIterator implements Iterator<Campaign>{


        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < totalItems;
        }

        @Override
        public Campaign next() {

            //if the next item is outside our current local storage
            if(index >= localCampaignStore.size()){

                // if this is still less then the total available items
                if(index < totalItems){
                    //fetch and load next page of data
                    loadNextPageOfDataIntoLocalList();
                    return localCampaignStore.get(index++);

                    //user is calling next on items that don't exist locally and don't exist on the server
                }else{
                    throw new NoSuchElementException();
                }

                //data is available in local storage
            }else{
                return localCampaignStore.get(index++);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
