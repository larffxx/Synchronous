package com.larffxx.synchronousdiscord.handler;

import com.larffxx.synchronousdiscord.exception.DownloadAttachmentException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentDownloader {

    public List<File> downloadAttachments(List<Message.Attachment> attachments) {
        List<File> files = new ArrayList<>();

        attachments.forEach(attachment -> {
            attachment.getProxy().downloadToFile(new File("C:/Users/offic/Desktop/tempphotos/" + attachment.getFileName())).thenAccept(path -> {
            }).exceptionally(throwable -> {
                throw new DownloadAttachmentException(InfExcMessages.DOWNLOAD_ATTACHMENT_EXCEPTION);
            });
            files.add(new File("C:/Users/offic/Desktop/tempphotos/" + attachment.getFileName()));
        });

        return files;
    }
}
