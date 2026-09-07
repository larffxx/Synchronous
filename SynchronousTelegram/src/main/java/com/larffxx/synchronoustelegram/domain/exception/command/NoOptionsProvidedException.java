package com.larffxx.synchronoustelegram.domain.exception.command;

/**
 * Signals that a command requiring options received none.
 */
public class NoOptionsProvidedException extends CommandException {
    public NoOptionsProvidedException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
