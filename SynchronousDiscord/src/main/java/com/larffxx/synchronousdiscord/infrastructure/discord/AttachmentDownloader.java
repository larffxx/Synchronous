package com.larffxx.synchronousdiscord.infrastructure.discord;

import net.dv8tion.jda.api.entities.Message;

import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Attachment Downloader class.
 */
@Component
public class AttachmentDownloader {

    /**
     * Downloads attachments.
     * @param attachments the attachments.
     */
    public List<File> downloadAttachments(List<Message.Attachment> attachments) {
        List<File> files = new ArrayList<>();

        attachments.forEach(attachment -> {
            File file = attachment.getProxy().downloadToFile(new File(Paths.get(System.getProperty("user.home"),
                            "Documents", "tempphotos", "photo" + attachment.getFileName()).toUri()))
                    .join();

            files.add(file);
        });

        return files;
    }
}
