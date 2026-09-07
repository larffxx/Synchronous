package com.larffxx.synchronousdiscord.domain.exception.interaction;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * Discord Slash Interaction Exception class.
 */
public class DiscordSlashInteractionException extends DiscordSynchronousException {
    /**
     * Creates a new DiscordSlashInteractionException.
     * @param message the message.
     */
    public DiscordSlashInteractionException(String message) {
        super(message);
    }
}
