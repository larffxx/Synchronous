package com.larffxx.synchronousdiscord.domain.exception.consume;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * Consuming Exception class.
 */
public class ConsumingException extends DiscordSynchronousException {
    /**
     * Creates a new ConsumingException.
     * @param message the message.
     */
    public ConsumingException(String message) {
        super(message);
    }

    /**
     * Creates a new ConsumingException with a cause.
     * @param message the message.
     * @param cause the cause.
     */
    public ConsumingException(String message, Throwable cause) {
        super(message, cause);
    }
}
