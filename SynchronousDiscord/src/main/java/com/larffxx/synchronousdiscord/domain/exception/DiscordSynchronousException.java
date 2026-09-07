package com.larffxx.synchronousdiscord.domain.exception;

/**
 * Discord Synchronous Exception class.
 */
public class DiscordSynchronousException extends RuntimeException {
    /**
     * Creates a new DiscordSynchronousException.
     * @param message the message.
     */
    public DiscordSynchronousException(String message) {
        super(message);
    }

    /**
     * Creates a new DiscordSynchronousException with a cause.
     * @param message the message.
     * @param cause the cause.
     */
    public DiscordSynchronousException(String message, Throwable cause) {
        super(message, cause);
    }
}
