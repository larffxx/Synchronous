package com.larffxx.synchronousdiscord.domain.exception.command;

import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;

/**
 * Command Exception class.
 */
public class CommandException extends DiscordSlashInteractionException {
    /**
     * Creates a new CommandException.
     * @param message the message.
     */
    public CommandException(String message) {
        super(message);
    }
}
