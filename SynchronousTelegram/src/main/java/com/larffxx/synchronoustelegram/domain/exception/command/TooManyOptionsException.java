package com.larffxx.synchronoustelegram.domain.exception.command;

/**
 * Signals that a command received more options than allowed.
 */
public class TooManyOptionsException extends CommandException {
    public TooManyOptionsException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
