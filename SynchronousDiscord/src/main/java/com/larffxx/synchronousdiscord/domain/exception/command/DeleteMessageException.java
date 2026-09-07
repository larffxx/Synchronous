package com.larffxx.synchronousdiscord.domain.exception.command;

/**
 * Delete Message Exception class.
 */
public class DeleteMessageException extends CommandException {
    /**
     * Creates a new DeleteMessageException.
     * @param message the message.
     */
    public DeleteMessageException(String message) {
        super(message);
    }
}
