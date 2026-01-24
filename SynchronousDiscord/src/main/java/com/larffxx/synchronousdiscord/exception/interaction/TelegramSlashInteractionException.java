package com.larffxx.synchronousdiscord.exception.interaction;

import com.larffxx.synchronousdiscord.exception.DiscordSynchronousException;

public class TelegramSlashInteractionException extends DiscordSynchronousException {
    public TelegramSlashInteractionException(String message) {
        super(message);
    }
}
