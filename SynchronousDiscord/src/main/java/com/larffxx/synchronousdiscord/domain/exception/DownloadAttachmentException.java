package com.larffxx.synchronousdiscord.domain.exception;

/**
 * Download Attachment Exception class.
 */
public class DownloadAttachmentException extends DiscordSynchronousException {
    /**
     * Creates a new DownloadAttachmentException.
     * @param message the message.
     */
    public DownloadAttachmentException(String message) {
        super(message);
    }
}
