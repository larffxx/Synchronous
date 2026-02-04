package com.larffxx.synchronousdiscord.util.downloader;

import com.larffxx.synchronousdiscord.domain.exception.DownloadAttachmentException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class AttachmentDownloader {

    public List<File> downloadAttachments(List<Message.Attachment> attachments) {
        List<File> files = new ArrayList<>();

        attachments.forEach(attachment -> {
            File file;
            try {
                file = attachment.getProxy().downloadToFile(new File(Paths.get(System.getProperty("user.home"),
                                "Documents", "tempphotos", "photo" + attachment.getFileName()).toUri()))
                        .join();

                files.add(file);
            } catch (DownloadAttachmentException e) {
                throw new DownloadAttachmentException(InfExcMessages.DOWNLOAD_ATTACHMENT_EXCEPTION);
            }
        });

        return files;
    }
}
