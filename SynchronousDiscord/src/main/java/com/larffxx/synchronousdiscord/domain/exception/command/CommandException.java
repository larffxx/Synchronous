package com.larffxx.synchronousdiscord.domain.exception.command;

import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;

public class CommandException extends DiscordSlashInteractionException {
    public CommandException(String message) {
        super(message);
    }
}
