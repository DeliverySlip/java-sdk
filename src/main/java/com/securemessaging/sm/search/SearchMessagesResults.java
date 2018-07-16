package com.securemessaging.sm.search;

import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.Message;
import com.securemessaging.sm.enums.BodyFormat;
import com.securemessaging.sm.enums.FyeoType;
import com.securemessaging.sm.request.GetSearchMessagesRequest;
import com.securemessaging.sm.response.GetSearchMessagesResponse;
import com.securemessaging.sm.response.MessageSummaryResponse;

import java.util.*;

public class SearchMessagesResults implements Iterable<Message>, Collection<Message> {

    private SearchMessagesFilter filter;
    private ClientRequestHandler client;

    private int currentPage;
    private int totalPages;
    private int pageSize;

    private int totalItems;


    private List<Message> localMessageStore = new ArrayList<Message>();

    public SearchMessagesResults(GetSearchMessagesResponse initialData, SearchMessagesFilter searchMessageFilter, ClientRequestHandler client){
        this.filter = searchMessageFilter;
        this.client = client;

        this.currentPage = initialData.currentPage;
        this.totalPages = initialData.totalPages;
        this.pageSize = initialData.pageSize;
        this.totalItems = initialData.totalItems;

        localMessageStore.addAll(processMessages(initialData.results));
    }

    private List<Message> processMessages(List<MessageSummaryResponse> responseResults){

        ArrayList<Message> messages = new ArrayList<Message>();

        for (MessageSummaryResponse messageSummaryResponse : responseResults) {
            Message message = new Message();

            List<String> toEmails = new ArrayList<String>();
            for(MessageSummaryResponse.User user: messageSummaryResponse.to){
                toEmails.add(user.email);
            }
            message.setTo(toEmails.toArray(new String[toEmails.size()]));

            List<String> ccEmails = new ArrayList<String>();
            for(MessageSummaryResponse.User user: messageSummaryResponse.cc){
                ccEmails.add(user.email);
            }
            message.setCC(ccEmails.toArray(new String[ccEmails.size()]));

            List<String> bccEmails = new ArrayList<String>();
            for(MessageSummaryResponse.User user: messageSummaryResponse.bcc){
                bccEmails.add(user.email);
            }
            message.setBCC(bccEmails.toArray(new String[bccEmails.size()]));

            message.setBody(messageSummaryResponse.body);
            message.setBodyFormat(BodyFormat.enumFromEnumText(messageSummaryResponse.bodyFormat));
            message.setSubject(messageSummaryResponse.subject);
            message.setMessageGuid(messageSummaryResponse.guid);

            message.setFrom(new String[] { messageSummaryResponse.sender.email});

            message.setForward(messageSummaryResponse.messageOptions.allowForward);
            message.setReply(messageSummaryResponse.messageOptions.allowReply);
            message.setTracking(messageSummaryResponse.messageOptions.allowTracking);
            message.setShareTracking(messageSummaryResponse.messageOptions.shareTracking);
            message.setFyeoType(FyeoType.enumFromEnumText(messageSummaryResponse.messageOptions.fyeoType));

            messages.add(message);
        }

        return messages;

    }

    public void fetchAllSearchResultsLocally(){
        Iterator<Message> iterator = iterator();
        while(iterator.hasNext()){
            iterator.next();
        }
    }


    private void loadNextPageOfDataIntoLocalList(){
        //refill the buffer with the next page
        GetSearchMessagesRequest request = new GetSearchMessagesRequest();
        request.searchCriteria = filter.getSearchCriteria();
        request.types = filter.getMessageBoxType().getEnumText();

        request.page = ++currentPage;
        request.pageSize = pageSize;

        try{
            GetSearchMessagesResponse response = client.makeRequest(request.getRequestRoute(), request, GetSearchMessagesResponse.class);
            currentPage = response.currentPage;
            totalItems = response.totalItems;
            totalPages = response.totalPages;

            List<Message> messages = processMessages(response.results);
            localMessageStore.addAll(messages);

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

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size() {
        return this.totalItems;
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty() {
        return this.localMessageStore.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this collection contains the specified
     * element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection
     *                              (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not permit null elements
     *                              (<a href="#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(Object o) {

        if(o instanceof Message){
            Message param = (Message)o;

            //try it with what we have to save some time
            for(Message message: this.localMessageStore){
                if(param.getMessageGuid().equals(message.getMessageGuid())){
                    return true;
                }
            }

            // fetch everything and try again
            fetchAllSearchResultsLocally();
            for(Message message: this.localMessageStore){
                if(message.getMessageGuid().equals(param.getMessageGuid())){
                    return true;
                }
            }

            return false;

        }else{
            throw new ClassCastException("Message Type Is Only Supported Object");
        }


    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Message> iterator() {
        return new SearchMessageResultsIterator();
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     * <p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     * <p>
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    @Override
    public Object[] toArray() {

        fetchAllSearchResultsLocally();
        return this.localMessageStore.toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     * <p>
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any <tt>null</tt> elements.)
     * <p>
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     * <p>
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     * <p>
     * <p>Suppose <tt>x</tt> is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of <tt>String</tt>:
     * <p>
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     * <p>
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param a the array into which the elements of this collection are to be
     *          stored, if it is big enough; otherwise, a new array of the same
     *          runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this collection
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        fetchAllSearchResultsLocally();
        return localMessageStore.toArray(a);
    }

    /**
     * Ensures that this collection contains the specified element (optional
     * operation).  Returns <tt>true</tt> if this collection changed as a
     * result of the call.  (Returns <tt>false</tt> if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     * <p>
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection.  In particular, some
     * collections will refuse to add <tt>null</tt> elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.<p>
     * <p>
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param message element whose presence in this collection is to be ensured
     * @return <tt>true</tt> if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this collection
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     * @throws IllegalArgumentException      if some property of the element
     *                                       prevents it from being added to this collection
     * @throws IllegalStateException         if the element cannot be added at this
     *                                       time due to insertion restrictions
     */
    @Override
    public boolean add(Message message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).  More formally,
     * removes an element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
     * this collection contains one or more such elements.  Returns
     * <tt>true</tt> if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     *
     * @param o element to be removed from this collection, if present
     * @return <tt>true</tt> if an element was removed as a result of this call
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this collection
     */
    @Override
    public boolean remove(Object o) {
        if(o instanceof Message){

            Message param = (Message)o;

            //try what we have currently if it exists
            if(this.contains(o)){

                for(Message message: this.localMessageStore){
                    if(param.getMessageGuid().equals(message.getMessageGuid())){
                        return this.localMessageStore.remove(message);
                    }
                }

            }else{
                //fetch everything and try again
                fetchAllSearchResultsLocally();

                for(Message message: this.localMessageStore){
                    if(param.getMessageGuid().equals(message.getMessageGuid())){
                        return this.localMessageStore.remove(message);
                    }
                }
            }

            return false;

        }else{
            throw new ClassCastException("Message Type Is Only Supported Object");
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection.
     *
     * @param c collection to be checked for containment in this collection
     * @return <tt>true</tt> if this collection contains all of the elements
     * in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection
     *                              (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not permit null
     *                              elements
     *                              (<a href="#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(Collection<?> c) {

        Iterator<?> iterator = c.iterator();

        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Message){
                if(!this.contains(next)){ //contains will ensure we check everything
                    return false;
                }
            }else{
                throw new ClassCastException("Message Type Is Only Supported Object");
            }
        }

        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param c collection containing elements to be added to this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @Override
    public boolean addAll(Collection<? extends Message> c) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param c collection containing elements to be removed from this collection
     * @return <tt>true</tt> if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not support
     *                                       null elements
     *                                       (<a href="#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(Collection<?> c) {

        Iterator<?> iterator = c.iterator();

        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Message){
                if(!this.remove(next)){ //contains will ensure we check everything
                    return false;
                }
            }else{
                throw new ClassCastException("Message Type Is Only Supported Object");
            }
        }

        return true;
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param c collection containing elements to be retained in this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not permit null
     *                                       elements
     *                                       (<a href="#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(Collection<?> c) {

        Iterator<Message> iterator = iterator();

        while(iterator.hasNext()){
            Message message = iterator.next();

            //if the passed in collection doesn't have it then remove it
            if(!c.contains(message)){

                //if removing fails though we have to return false that this failed
                if(!this.remove(message)){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() {
        currentPage = 0;
        this.localMessageStore.clear();

        loadNextPageOfDataIntoLocalList(); // grab the first page so that our meta is corrected
    }


    private class SearchMessageResultsIterator implements  Iterator<Message>{

        private int index;

        public SearchMessageResultsIterator(){
            index = 0;
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return this.index < totalItems;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public Message next() {

            //if the next item is outside our current local storage
            if(index >= localMessageStore.size()){

                // if this is still less then the total available items
                if(index < totalItems){
                    //fetch and load next page of data
                    loadNextPageOfDataIntoLocalList();
                    return localMessageStore.get(index++);

                //user is calling next on items that don't exist locally and don't exist on the server
                }else{
                    throw new NoSuchElementException();
                }

            //data is available in local storage
            }else{
                return localMessageStore.get(index++);
            }
        }

        /**
         * Removes from the underlying collection the last element returned
         * by this iterator (optional operation).  This method can be called
         * only once per call to {@link #next}.  The behavior of an iterator
         * is unspecified if the underlying collection is modified while the
         * iteration is in progress in any way other than by calling this
         * method.
         *
         * @throws UnsupportedOperationException if the {@code remove}
         *                                       operation is not supported by this iterator
         * @throws IllegalStateException         if the {@code next} method has not
         *                                       yet been called, or the {@code remove} method has already
         *                                       been called after the last call to the {@code next}
         *                                       method
         * @implSpec The default implementation throws an instance of
         * {@link UnsupportedOperationException} and performs no other action.
         */
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


}
