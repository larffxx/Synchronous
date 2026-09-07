package com.larffxx.synchronoustelegram.domain.exception.data.input;

import com.larffxx.synchronoustelegram.domain.exception.data.DataException;

/**
 * Signals that a photo could not be received from Telegram.
 */
public class ReceivingPhotoException extends DataException {
    public ReceivingPhotoException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
