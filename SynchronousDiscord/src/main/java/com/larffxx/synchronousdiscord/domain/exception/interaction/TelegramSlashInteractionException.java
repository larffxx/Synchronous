package com.larffxx.synchronousdiscord.domain.exception.interaction;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class TelegramSlashInteractionException extends DiscordSynchronousException {
    public TelegramSlashInteractionException(String message) {
        super(message);
    }
}
