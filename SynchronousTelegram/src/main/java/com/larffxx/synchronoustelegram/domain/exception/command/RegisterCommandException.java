package com.larffxx.synchronoustelegram.domain.exception.command;

/**
 * Signals that the register command failed.
 */
public class RegisterCommandException extends CommandException {
    public RegisterCommandException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
