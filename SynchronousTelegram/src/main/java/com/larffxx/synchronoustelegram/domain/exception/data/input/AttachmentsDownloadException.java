package com.larffxx.synchronoustelegram.domain.exception.data.input;

import com.larffxx.synchronoustelegram.domain.exception.data.DataException;

/**
 * Signals that message attachments could not be downloaded.
 */
public class AttachmentsDownloadException extends DataException {
    public AttachmentsDownloadException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
