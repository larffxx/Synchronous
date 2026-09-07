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
}
