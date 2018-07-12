package com.securemessaging.javamessenger.sm.attachments;

import com.securemessaging.javamessenger.client.ClientRequestHandler;
import com.securemessaging.javamessenger.ex.SecureMessengerClientException;
import com.securemessaging.javamessenger.ex.SecureMessengerException;
import com.securemessaging.javamessenger.sm.AttachmentChunk;
import com.securemessaging.javamessenger.sm.Message;
import com.securemessaging.javamessenger.sm.request.PostPreCreateAttachmentsRequest;
import com.securemessaging.javamessenger.sm.response.PostPreCreateAttachmentsResponse;
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

public class AttachmentManager {

    public interface OnAttachmentUploadEventListenerInterface{

        void onProgress(AttachmentPlaceholder placeholder, int totalChunks, int chunksUploaded);

        void onStarted(AttachmentPlaceholder placeholder);

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

    public boolean addAttachmentFile(File file){
        if(!this.attachmentsHaveBeenPreCreated && file.exists() && file.canRead()){
            this.attachmentList.add(file);
            return true;
        }
        return false;
    }

    public boolean attachmentsHaveBeenPreCreated(){
        return this.attachmentsHaveBeenPreCreated;
    }

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

    public List<AttachmentPlaceholder> getAllPreCreatedAttachments(){
        if(this.preCreateAttachmentsResponse == null){
            return new ArrayList<AttachmentPlaceholder>();
        }
        return this.preCreateAttachmentsResponse.attachmentPlaceholders;
    }

    public void setOnAttachmentUploadProgressListener(OnAttachmentUploadEventListenerInterface listener){
        this.onAttachmentUploadEventListenerInterface = listener;
    }

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
