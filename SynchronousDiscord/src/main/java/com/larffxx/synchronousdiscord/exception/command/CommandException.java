package com.larffxx.synchronousdiscord.exception.command;

import com.larffxx.synchronousdiscord.exception.interaction.DiscordSlashInteractionException;

public class CommandException extends DiscordSlashInteractionException {
    public CommandException(String message) {
        super(message);
    }
}
