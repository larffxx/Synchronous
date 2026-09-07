package com.larffxx.synchronoustelegram.domain.exception.command;

/**
 * Signals that a command or button callback has an unexpected format.
 */
public class InvalidCommandException extends CommandException {
    public InvalidCommandException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
