package com.larffxx.synchronoustelegram.domain.exception.execution;

/**
 * Signals that a text message could not be sent to Telegram.
 */
public class SendingMessageToTelegramException extends ExecutionException {
    public SendingMessageToTelegramException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
