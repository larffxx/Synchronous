package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * No Connection Between Servers Exception class.
 */
public class NoConnectionBetweenServersException extends DiscordSynchronousException {
    /**
     * Creates a new NoConnectionBetweenServersException.
     * @param message the message.
     */
    public NoConnectionBetweenServersException(String message) {
        super(message);
    }
}
