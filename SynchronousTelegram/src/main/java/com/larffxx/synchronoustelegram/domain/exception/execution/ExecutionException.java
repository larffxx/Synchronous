package com.larffxx.synchronoustelegram.domain.exception.execution;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

/**
 * Base exception for failures while executing Telegram operations. Parent of message sending and command execution failures.
 */
public class ExecutionException extends TelegramException {
    public ExecutionException(String message) {
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
    public ExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}
