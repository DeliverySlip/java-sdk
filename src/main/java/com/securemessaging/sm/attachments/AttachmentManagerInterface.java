package com.securemessaging.sm.attachments;

import com.securemessaging.ex.SecureMessengerClientException;
import com.securemessaging.ex.SecureMessengerException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface AttachmentManagerInterface {


    boolean addAttachmentFile(File file);

    boolean attachmentsHaveBeenPreCreated();

    void preCreateAllAttachments() throws SecureMessengerException, SecureMessengerClientException;

    void uploadAllAttachments() throws SecureMessengerClientException, IOException;

    void uploadAttachment(AttachmentPlaceholder attachment) throws IOException, SecureMessengerClientException;

    List<AttachmentPlaceholder> getAllPreCreatedAttachments();

    void setOnAttachmentUploadProgressListener(AttachmentManager.OnAttachmentUploadEventListenerInterface listener);

}
