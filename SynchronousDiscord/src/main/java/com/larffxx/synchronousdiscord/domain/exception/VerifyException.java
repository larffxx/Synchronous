package com.larffxx.synchronousdiscord.domain.exception;

/**
 * Verify Exception class.
 */
public class VerifyException extends DiscordSynchronousException{
    /**
     * Creates a new VerifyException.
     * @param message the message.
     */
    public VerifyException(String message) {
        super(message);
    }
}
