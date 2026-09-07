package com.larffxx.synchronoustelegram.domain.exception.command;

/**
 * Signals that the connect command failed.
 */
public class ConnectCommandException extends CommandException {
    public ConnectCommandException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
