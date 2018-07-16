package com.securemessaging.sm.attachments;

import com.securemessaging.client.ClientRequestHandler;
import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;
import com.securemessaging.sm.AttachmentChunk;
import com.securemessaging.sm.Message;
import com.securemessaging.sm.request.PostPreCreateAttachmentsRequest;
import com.securemessaging.sm.response.PostPreCreateAttachmentsResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * AttachmentManager handles uploading and downloading of attachments along with event handling
 */
public class AttachmentManager {

    /**
     * OnAttachmentUploadEventListenerInterface defines the event methods that are called during an upload
     */
    public interface OnAttachmentUploadEventListenerInterface{

        /**
         * onProgress is called everytime a chunk is uploaded to the API
         * @param placeholder - the placeholder representing the attachment being uploaded
         * @param totalChunks - the total number of chunks being uploaded
         * @param chunksUploaded - the number of chunks so far that have been uploaded
         */
        void onProgress(AttachmentPlaceholder placeholder, int totalChunks, int chunksUploaded);

        /**
         * onStarted is called when the upload has begun and before the first chunk is uploaded to the API. This is called
         * for each attachment
         * @param placeholder - the placeholder representing the attachment
         */
        void onStarted(AttachmentPlaceholder placeholder);

        /**
         * onComplete is called when the upload has completed after the last chunk is uploaded to the API. This is called
         * for each attachment
         * @param placeholder - the placeholder representing the attachment
         */
        void onComplete(AttachmentPlaceholder placeholder);
    }

    private OnAttachmentUploadEventListenerInterface onAttachmentUploadEventListenerInterface = null;

    private List<File> attachmentList;
    private ClientRequestHandler client;
    private Message message;
    private PostPreCreateAttachmentsResponse preCreateAttachmentsResponse;

    private boolean attachmentsHaveBeenPreCreated;

    public AttachmentManager(Message message, ClientRequestHandler client){
        this.attachmentList = new ArrayList<File>();
        this.message = message;
        this.attachmentsHaveBeenPreCreated = false;

        this.client = client;
    }

    /**
     * addAttachmentFile adds the passed file to the list of attachments to be uploaded. This method can only
     * be called before preCreateAllAttachments(). If preCreateAllAttachments() has already been called this method
     * will return false - meaning the attachment has not been added. Call attachmentsHaveBeenPreCreated() to check
     * if preCreateAllAttachments() has been called
     * @param file - the file to be uploaded
     * @return - whether the file was successfully added to be uploaded
     */
    public boolean addAttachmentFile(File file){
        if(!this.attachmentsHaveBeenPreCreated && file.exists() && file.canRead()){
            this.attachmentList.add(file);
            return true;
        }
        return false;
    }

    /**
     * attachmentsHaveBeenPreCreated checks whether the added attachments have been precreated on the server yet
     * @return - whether the attachments have been precreated or not
     */
    public boolean attachmentsHaveBeenPreCreated(){
        return this.attachmentsHaveBeenPreCreated;
    }

    /**
     * preCreateAllAttachments executes processing to pre create all attachments. This allocates resources on the
     * server for the incoming attachments being uploaded
     * @throws SecureMessengerException - the server returned an error while pre creating the attachments
     * @throws SecureMessengerClientException - there was an error on the client side, validating, processing or
     * handling the precreateallattachments call
     */
    public void preCreateAllAttachments() throws SecureMessengerException, SecureMessengerClientException{

        ArrayList<PreCreateAttachmentPlaceholder> attachmentPlaceholders = new ArrayList<PreCreateAttachmentPlaceholder>();

        for(int i = 0; i < attachmentList.size(); i++){
            File file = attachmentList.get(i);

            PreCreateAttachmentPlaceholder placeholder = new PreCreateAttachmentPlaceholder(file.getName(), file.length());
            attachmentPlaceholders.add(placeholder);
        }

        PostPreCreateAttachmentsRequest request = new PostPreCreateAttachmentsRequest();
        request.setAttachmentPlaceholders(attachmentPlaceholders);
        request.setMessageGuid(message.getMessageGuid());

        this.preCreateAttachmentsResponse = this.client.makeRequest(request.getRequestRoute(), request, PostPreCreateAttachmentsResponse.class);
        this.attachmentsHaveBeenPreCreated = true;
    }

    /**
     * uploadAllAttachments triggers the upload process of all attachments that have been added and precreated
     * @throws SecureMessengerClientException
     * @throws IOException - there is an IO related issue with one of the attachments
     */
    public void uploadAllAttachments() throws SecureMessengerClientException, IOException{
        if(this.attachmentsHaveBeenPreCreated){
            for(int j = 0; j < this.preCreateAttachmentsResponse.attachmentPlaceholders.size(); j++){

                AttachmentPlaceholder placeholder = this.preCreateAttachmentsResponse.attachmentPlaceholders.get(j);
                this.uploadAttachment(placeholder);
            }
        }else{
            throw new SecureMessengerClientException("All Attachments Must Be PreCreated Before They Can Be Uploaded");
        }
    }

    /**
     * uploadAttachment allows the client to explicityly specify which attachments to upload. All attachments must
     * be precreated before this method can be called
     * @param attachment - the attachment to be uploaded
     * @throws IOException - there is an IO related error with the attachment
     * @throws SecureMessengerClientException
     */
    public void uploadAttachment(AttachmentPlaceholder attachment) throws IOException, SecureMessengerClientException{
        if(this.attachmentsHaveBeenPreCreated){
            boolean attachmentFileFound = false;
            for(int k = 0; k < this.attachmentList.size(); k++){
                if(this.attachmentList.get(k).getName().equalsIgnoreCase(attachment.fileName)){
                    this.uploadAttachmentChunk(attachment, message.getMessageGuid(), this.attachmentList.get(k));
                    attachmentFileFound = true;
                    break;
                }
            }

            if(!attachmentFileFound){
                throw new SecureMessengerClientException("A File Containing The Same Name As A PreCreated " +
                        "Attachment Could Not Be Found. Does The File Exist ? Has It Been Added Before PreCreateConfiguration ?");
            }
        }else{
            throw new SecureMessengerClientException("All Attachments Must Be PreCreated Before They Can Be Uploaded");
        }
    }

    /**
     * getAllPreCreatedAttachments returns a list of all the attachments that have been precreated. If none have been
     * precreated, an empty list is returned
     * @return a list of attachmentplaceholders of all the precreated attachments
     */
    public List<AttachmentPlaceholder> getAllPreCreatedAttachments(){
        if(this.preCreateAttachmentsResponse == null){
            return new ArrayList<AttachmentPlaceholder>();
        }
        return this.preCreateAttachmentsResponse.attachmentPlaceholders;
    }

    /**
     * setOnAttachmentUploadProgressListener sets and unsets the event handler for attachment progress as its being
     * uploaded. By passing null you can unset the listener. Only one listener can be set at a time
     * @param listener - the event handler for events thrown by the AttachmentManager during upload
     */
    public void setOnAttachmentUploadProgressListener(OnAttachmentUploadEventListenerInterface listener){
        this.onAttachmentUploadEventListenerInterface = listener;
    }

    /**
     * uploadAttachmentChunk is the processing method that uploaded each chunk of a given attachment
     * @param attachment - the attachment the attachment chunks belong to
     * @param messageGuid - the message the chunk is being uploaded to
     * @param file - the file object being uploaded
     * @throws IOException - an IO related error with the attachment file has occurred or a chunk/file is corrupted
     * @throws FileNotFoundException - the attachment file could not found
     */
    private void uploadAttachmentChunk(AttachmentPlaceholder attachment, String messageGuid, File file) throws IOException, FileNotFoundException {

        try{
            FileInputStream fileStream = new FileInputStream(file);
            ArrayList<AttachmentPlaceholderChunk> attachmentChunks = attachment.getChunks();

            if(this.onAttachmentUploadEventListenerInterface != null){
                this.onAttachmentUploadEventListenerInterface.onStarted(attachment);
                this.onAttachmentUploadEventListenerInterface.onProgress(attachment, attachmentChunks.size(), 0);
            }

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
                    String result = client.getRestTemplate().postForEntity(uploadUri, requestEntity, AttachmentChunk.class).toString();

                    if(this.onAttachmentUploadEventListenerInterface != null){
                        this.onAttachmentUploadEventListenerInterface.onProgress(attachment, attachmentChunks.size(), j+1);
                    }

                }catch(HttpClientErrorException e){
                    e.printStackTrace();

                    System.out.println(e.getResponseHeaders().toString());
                    System.out.println(e.getResponseBodyAsString());
                }

            }

            if(this.onAttachmentUploadEventListenerInterface != null){
                this.onAttachmentUploadEventListenerInterface.onComplete(attachment);
            }

        }catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
            throw fnfe;
        }catch(IOException ioe){
            ioe.printStackTrace();
            throw ioe;
        }
    }
}
