package com.larffxx.synchronoustelegram.domain.exception;

/**
 * Base runtime exception for all Telegram module failures. Parent of command, data, and execution failures.
 */
public class TelegramException extends RuntimeException {
    public TelegramException(String message) {
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
    public TelegramException(String message, Throwable cause) {
        super(message, cause);
    }
}
