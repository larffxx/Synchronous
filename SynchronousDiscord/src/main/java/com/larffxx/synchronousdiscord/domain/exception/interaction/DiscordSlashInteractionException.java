package com.larffxx.synchronousdiscord.domain.exception.interaction;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class DiscordSlashInteractionException extends DiscordSynchronousException {
    public DiscordSlashInteractionException(String message) {
        super(message);
    }
}
