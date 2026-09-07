package com.larffxx.synchronousdiscord.domain.exception.interaction;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

/**
 * Telegram Slash Interaction Exception class.
 */
public class TelegramSlashInteractionException extends DiscordSynchronousException {
    /**
     * Creates a new TelegramSlashInteractionException.
     * @param message the message.
     */
    public TelegramSlashInteractionException(String message) {
        super(message);
    }
}
