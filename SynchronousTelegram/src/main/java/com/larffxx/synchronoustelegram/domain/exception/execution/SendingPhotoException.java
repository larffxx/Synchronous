package com.larffxx.synchronoustelegram.domain.exception.execution;

/**
 * Signals that a photo could not be sent to Telegram.
 */
public class SendingPhotoException extends ExecutionException {
    public SendingPhotoException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }

    /**
     * Creates the exception with a detail message and a cause.
     * @param message the detail message
     * @param cause the cause
     */
    public SendingPhotoException(String message, Throwable cause) {
        super(message, cause);
    }
}
