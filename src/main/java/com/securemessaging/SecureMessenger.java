package com.securemessaging;

import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.*;
import com.securemessaging.sm.attachments.AttachmentManager;
import com.securemessaging.sm.attachments.AttachmentPlaceholder;
import com.securemessaging.sm.attachments.AttachmentPlaceholderChunk;
import com.securemessaging.sm.auth.SMAuthenticationInterface;
import com.securemessaging.sm.auth.ServiceCodeResolver;
import com.securemessaging.sm.auth.SessionFactory;
import com.securemessaging.sm.enums.ActionCode;
import com.securemessaging.sm.enums.notifications.NotificationType;
import com.securemessaging.sm.notifications.NotificationManager;
import com.securemessaging.sm.request.*;
import com.securemessaging.sm.response.*;
import com.securemessaging.sm.search.SearchMessagesResults;
import com.securemessaging.sm.search.SearchMessagesFilter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The SecureMessenger is a wrapper class to making API calls to carry out functions in creating and managing secure
 * emails
 */
public class SecureMessenger {

    /** restTemplate RestTemplate  - instance of the Sprint restTemplate to make are REST API calls **/
    //private RestTemplate restTemplate;
    /** authInterceptor HeaderRequestInterceptor - an interceptor for the RestTemplate to control the headers required
     * for authentication and working with the SecureMessaging API **/
    //private HeaderRequestInterceptor authInterceptor;
    /** baseURL String - the base URL of the portal api being called **/
    private String baseURL;

    private ClientRequestHandler client;

    public static SecureMessenger resolveViaServiceCode(String serviceCode) throws SecureMessengerClientException, SecureMessengerException{

        try{
            String baseURL = ServiceCodeResolver.resolve(serviceCode);

            if(baseURL.endsWith("/")){
                baseURL = baseURL.substring(0, baseURL.length() - 1);
            }

            SecureMessenger messenger = new SecureMessenger(baseURL + "/v1");
            return messenger;
        }catch(SecureMessengerClientException smce){
            throw new SecureMessengerClientException("The ServiceCode Provided Is Invalid");
        }
    }


    /**
     * Constructor - initializes the SecureMessenger to be able to process requests appropriately. It initializes the
     * restTemplate and the authInterceptor and sets the baseURL
     * @param baseURL String - the baseURL of the portal api being called by the SecureMessenger
     */
    public SecureMessenger(String baseURL){
        this.client = new ClientRequestHandler(baseURL);
    }

    public SecureMessenger(ClientRequestHandler client){
        this.client = client;
    }

    public void setClientName(String clientName){
        this.client.setClientName(clientName);
    }

    public void setClientVersion(String clientVersion){
        this.client.setClientVersion(clientVersion);
    }

    public String getClientVersion(){
        return this.client.getClientVersion();
    }

    /**
     * Login allows the user to authenticate with the Messaging API by passing a credentials object. The SecureMessenger
     * then manages the auth token, required as a header, returned by sending it to the authInterceptor
     */
    public void login(SMAuthenticationInterface authentication) throws SecureMessengerException, SecureMessengerClientException{

        Session session = SessionFactory.createSession(authentication, this.client.getBaseURL());
        this.client.setSession(session);
    }

    public Message preCreateMessage(PreCreateConfiguration preCreateConfiguration)throws SecureMessengerException, SecureMessengerClientException{

        PostPreCreateMessageRequest request = new PostPreCreateMessageRequest();
        request.setActionCode(preCreateConfiguration.getActionCode());
        request.parentGuid = preCreateConfiguration.getParentGuid();
        request.password = preCreateConfiguration.getPassword();

        PostPreCreateMessageResponse response = this.client.makeRequest(request.getRequestRoute(), request, PostPreCreateMessageResponse.class);

        Message message = new Message();
        message.setMessageGuid(response.messageGuid);

        //TODO: Call GetMessage to fill in additional information about the message instead of JUST the GUID ?

        //don't need to specify from if its the same as the account
        message.setFrom(new String[]{this.client.getSession().emailAddress});
        return message;

    }

    /**
     * preCreateMessage pre-creates a message on the Messaging API and returns a Message object which can be used by the
     * client to create and modify their secure message. The Message object also contains its own message GUID which
     * is used to save, send and do other manipulations of the message on the Messaging API
     * @return email Message - an object representing the secure message
     */
    public Message preCreateMessage() throws SecureMessengerException, SecureMessengerClientException{

        PreCreateConfiguration configuration = new PreCreateConfiguration();
        configuration.setActionCode(ActionCode.NEW);

        return preCreateMessage(configuration);

    }

    public SearchMessagesResults searchMessages(SearchMessagesFilter searchMessagesFilter)throws SecureMessengerException, SecureMessengerClientException{

        GetSearchMessagesRequest request = new GetSearchMessagesRequest();
        request.searchCriteria = searchMessagesFilter.getSearchCriteria();
        request.types = searchMessagesFilter.getMessageBoxType().getEnumText();
        request.page = searchMessagesFilter.getPageNumber();
        request.pageSize = searchMessagesFilter.getPageSize();

        GetSearchMessagesResponse response = client.makeRequest(request.getRequestRoute(), request, GetSearchMessagesResponse.class);

        return new SearchMessagesResults(response, searchMessagesFilter, this.client);
    }


    public NotificationManager getNotificationManagerForSession()
            throws SecureMessengerException, SecureMessengerClientException{

        PostCreateNotificationSessionRequest request = new PostCreateNotificationSessionRequest();
        request.enableUserTrackingNotifications = true;
        request.newAssetNotificationFilters = new ArrayList<String>();


        for(NotificationType notificationType: NotificationType.values()){
            request.newAssetNotificationFilters.add(notificationType.getEnumText());
        }

        PostCreateNotificationSessionResponse response = this.client.makeRequest(request.getRequestRoute(), request, PostCreateNotificationSessionResponse.class);


        NotificationManager manager = new NotificationManager(response.notificationSessionToken, this.client.getBaseURL());
        return manager;
    }


    /*public NotificationManager getNotificationManagerForMessage(Message message)throws SecureMessengerException, SecureMessengerClientException{

        PostCreateNotificationSessionRequest request = new PostCreateNotificationSessionRequest();
        request.enableUserTrackingNotifications = true;
        request.newAssetNotificationFilters = new ArrayList<String>();

        for(NotificationType notificationType: NotificationType.values()){
            request.newAssetNotificationFilters.add(notificationType.getEnumText());
        }

        PostCreateNotificationSessionResponse response = this.client.makeRequest(request.getRequestRoute(), request, PostCreateNotificationSessionResponse.class);

        PostSubscribeRequest subscribeRequest = new PostSubscribeRequest();
        subscribeRequest.notificationSessionToken = response.notificationSessionToken;
        subscribeRequest.subscriptions = new ArrayList<PostSubscribeRequest.Subscription>();

        PostSubscribeRequest.Subscription subscription = new PostSubscribeRequest.Subscription();
        subscription.subscriptType = SubscriptionType.MESSAGE.getEnumText();
        subscription.subscriptionGuid = message.getMessageGuid();

        subscribeRequest.subscriptions.add(subscription);

        //PostSubscribeResponse subscribeResponse = this.client.makeRequest(subscribeRequest.getRequestRoute(), subscribeRequest, PostSubscribeResponse.class);

        NotificationManager manager = new NotificationManager(response.notificationSessionToken, subscribeRequest.subscriptions);
        return manager;
    }*/



    /**
     * saveMessage saves the passed in message object into the logged in user's drafts. Regardless of whether the client
     * wants to save the message to drafts, this is a required step before calling 'sendMessage'.
     * @param email Message - the message object to be saved
     */
    public Message saveMessage(Message email) throws SecureMessengerException, SecureMessengerClientException{

        PutSaveMessageRequest request = new PutSaveMessageRequest();
        request.messageGuid = email.getMessageGuid();
        request.to = email.getTo();
        request.cc = email.getCC();
        request.bcc = email.getBCC();
        request.subject = email.getSubject();
        request.body = email.getBody();
        request.setBodyFormat(email.getBodyFormat());

        request.messageOptions.setFYEOType(email.getFyeoType());
        request.messageOptions.allowForward = email.getForwardStatus();
        request.messageOptions.allowReply = email.getReplyStatus();
        request.messageOptions.allowTracking = email.getTrackingStatus();
        request.messageOptions.shareTracking = email.getShareTrackingStatus();

        PutSaveMessageResponse response = this.client.makeRequest(request.getRequestRoute(), request, PutSaveMessageResponse.class);

        email.hasBeenSaved = true;

        return email;

    }

    /**
     * sendMessage sends the passed in message object. The passed in message object must have been saved before it
     * can be sent otherwise an exception will be thrown
     * @param email Message - the email object to be sent
     */
    public void sendMessage(Message email) throws SecureMessengerClientException, SecureMessengerException{

        if(!email.hasBeenSaved){
            throw new SecureMessengerClientException("The Message Must Be Saved Before It Can Be Sent. Ensure you have called" +
                    "saveMessage() before calling sendMessage()");
        }

        PutSendMessageRequest request = new PutSendMessageRequest();
        request.messageGuid = email.getMessageGuid();
        request.inviteNewUsers = email.getInviteNewUsersStatus();
        request.sendEmailNotification = email.getSendNotificationStatus();
        request.craCode = email.getCraCode();
        request.password = email.getPassword();

        PutSendMessageResponse response = this.client.makeRequest(request.getRequestRoute(), request, PutSendMessageResponse.class);

    }


    /**
     * sendSimpleMessage goes through the steps of creating and sending a message with simple settings. The client only
     * needs to supply a message object with the desired content
     * @param email Message - the email object passed from the client to be created and sent
     * @deprecated - Method will be removed in 4.0.0 release
     */
    @Deprecated
    public void sendSimpleMessage(Message email) throws Exception{
        Message message = this.preCreateMessage();
        email.setMessageGuid(message.getMessageGuid());
        this.saveMessage(email);
        this.sendMessage(email);
    }

    public AttachmentManager createAttachmentManagerForMessage(Message email){
        return new AttachmentManager(email, this.client);
    }


    public void uploadAttachmentsForMessage(Message email, File[] attachments)
            throws SecureMessengerClientException, SecureMessengerException,
                                            IOException{

        AttachmentManager manager = this.createAttachmentManagerForMessage(email);
        for(int i = 0; i < attachments.length; i++){
            manager.addAttachmentFile(attachments[i]);
        }

        manager.preCreateAllAttachments();
        manager.uploadAllAttachments();
    }


    /**
     * preCreateAttachment executes calling the preCreateAttachment API call using the passed in PostPreCreateAttachmentsRequest object.
     * The client needs to only supply the required components of the PostPreCreateAttachmentsRequest object
     * @param preCreateAttachment PostPreCreateAttachmentsRequest - the precreateattachment object representing specification needed
     *                            to properly allocate resources for the attachment to be uploaded
     * @return Attachment - the attachment object needed for the next steps in uploading attachments
     * @deprecated - Method has been refactored into AttachmentManager workflow. Use uploadAttachmentsForMessage() or
     * AttachmentManager workflows. Method will be removed in 4.0.0 release
     */
    @Deprecated
    public Attachment preCreateAttachment(PostPreCreateAttachmentsRequest preCreateAttachment) throws SecureMessengerException, SecureMessengerClientException{
        System.out.println("WARNING - PreCreateAttachment Is Deprecated");

        PostPreCreateAttachmentsResponse response = this.client.makeRequest(preCreateAttachment.getRequestRoute(), preCreateAttachment, PostPreCreateAttachmentsResponse.class);

        Attachment attachment = new Attachment();
        attachment.attachmentPlaceholders = response.attachmentPlaceholders;

        return attachment;
    }

    /**
     *uploadAttachmentChunk is the second state of the upload attachment process. Each attachment allocated in the preCreateAttachment
     * call is then uploaded using this call. Each attachment is cutup into chunks and then called against the API
     * @param attachment - placeholder representing the meta information about the attachment. This data is returned in the preCreateAttachment response
     * @param messageGuid - the unique identiifer of the message the attachments being uploaded are for
     * @param file - a file object reference to the fule being uploaded - belonging to the meta information passed in the AttachmentPlaceholder parameter
     * @throws IOException - Error reading the fole
     * @throws FileNotFoundException - The specified file object could not be found
     * @deprecated - Method has been refactored into AttachmentManager workflow. Use uploadAttachmentsForMessage() or
     * AttachmentManager workflows. Method will be removed in 4.0.0 release
     */
    @Deprecated
    public void uploadAttachmentChunk(AttachmentPlaceholder attachment, String messageGuid, File file) throws IOException, FileNotFoundException{
        System.out.println("WARNING - UploadAttachmentChunk Is Deprecated");
        try{

            FileInputStream fileStream = new FileInputStream(file);
            ArrayList<AttachmentPlaceholderChunk> attachmentChunks = attachment.getChunks();
            //System.out.println("There Is A Total Of" + attachmentChunks.size() + " Chunks To Upload");
            for(int j = 0; j < attachmentChunks.size(); j++){
                int chunkNumber = attachmentChunks.get(j).chunkNumber;
                String uploadUri = attachmentChunks.get(j).uploadUri;
                long byteStartIndex = attachmentChunks.get(j).byteStartIndex;
                long byteEndIndex = attachmentChunks.get(j).byteEndIndex;
                long length = attachmentChunks.get(j).bytesSize;

                MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<String, Object>();

                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

                byte[] data = new byte[(int)(byteEndIndex - byteStartIndex)];
                fileStream.read(data, 0, ((int)(byteEndIndex - byteStartIndex)));

                HttpHeaders fileHeader = new HttpHeaders();
                fileHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                fileHeader.setContentDispositionFormData("upload", file.getName());

                ByteArrayResource r = new ByteArrayResource(data);
                HttpEntity<ByteArrayResource> fileChunkEntity = new HttpEntity<ByteArrayResource>(r, fileHeader);

                multipartRequest.add("upload", fileChunkEntity);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(multipartRequest, requestHeaders);

                try{
                    this.client.getAuthInterceptor().skipContentTypeAndAcceptHeadersForNextCall();
                    System.out.println(uploadUri);
                    String result = client.getRestTemplate().postForEntity(uploadUri, requestEntity, AttachmentChunk.class).toString();
                    //System.out.println(result);
                }catch(HttpClientErrorException e){
                    e.printStackTrace();

                    System.out.println(e.getResponseHeaders().toString());
                    System.out.println(e.getResponseBodyAsString());
                }
            }



        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            throw fnfe;
        }catch(IOException ioe){
            ioe.printStackTrace();
            throw ioe;
        }
    }

    public String getAuthenticationToken(int numberOfDaysUntilExpiry) throws SecureMessengerException, SecureMessengerClientException{

        GetNewAuthenticationTokenRequest request = new GetNewAuthenticationTokenRequest();
        request.authenticationTokenMaxDays = numberOfDaysUntilExpiry;

        GetNewAuthenticationTokenResponse response = this.client.makeRequest(request.getRequestRoute(), request, GetNewAuthenticationTokenResponse.class);
        return response.authenticationToken;
    }

    public void deleteAuthenticationToken(String authenticationToken) throws SecureMessengerException, SecureMessengerClientException{

        DeleteExpireAuthenticationTokenRequest request = new DeleteExpireAuthenticationTokenRequest();
        request.authenticationToken = authenticationToken;

        this.client.makeRequest(request.getRequestRoute(), request, String.class);
    }

    public void deleteAllAuthenticationTokens() throws SecureMessengerException, SecureMessengerClientException{

        DeleteExpireAuthenticationTokensRequest request = new DeleteExpireAuthenticationTokensRequest();

        this.client.makeRequest(request.getRequestRoute(), request, String.class);
    }


}
