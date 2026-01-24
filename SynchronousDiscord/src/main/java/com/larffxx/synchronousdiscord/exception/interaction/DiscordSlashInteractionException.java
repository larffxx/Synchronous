package com.larffxx.synchronousdiscord.exception.interaction;

import com.larffxx.synchronousdiscord.exception.DiscordSynchronousException;

public class DiscordSlashInteractionException extends DiscordSynchronousException {
    public DiscordSlashInteractionException(String message) {
        super(message);
    }
}
