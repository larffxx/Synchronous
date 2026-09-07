package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * Guild Not Found Exception class.
 */
public class GuildNotFoundException extends DiscordSynchronousException {
    /**
     * Creates a new GuildNotFoundException.
     * @param message the message.
     */
    public GuildNotFoundException(String message) {
        super(message);
    }
}
