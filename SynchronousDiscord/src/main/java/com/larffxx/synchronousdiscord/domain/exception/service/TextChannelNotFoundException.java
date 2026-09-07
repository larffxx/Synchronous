package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * Text Channel Not Found Exception class.
 */
public class TextChannelNotFoundException extends DiscordSynchronousException {
    /**
     * Creates a new TextChannelNotFoundException.
     * @param message the message.
     */
    public TextChannelNotFoundException(String message) {
        super(message);
    }
}
