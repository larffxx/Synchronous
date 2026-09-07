package com.larffxx.synchronoustelegram.domain.exception.execution;

/**
 * Signals that a raw string command could not be executed.
 */
public class StringCommandExecutingException extends ExecutionException {
    public StringCommandExecutingException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
