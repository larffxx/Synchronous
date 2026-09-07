package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.exception.command.CommandException;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

/**
 * Contract for a slash command supported from Discord and Telegram.
 */
@Component
public interface Command {
    /**
     * Executes the command for a Discord slash command interaction.
     *
     * @param t Discord slash command interaction event
     * @throws CommandException when command execution fails
     */
    void execute(SlashCommandInteractionEvent t) throws CommandException;
    /**
     * Executes the command for a Telegram command context.
     *
     * @param telegramCommandContext Telegram command context
     * @throws CommandException when command execution fails
     */
    void execute(TelegramCommandContext telegramCommandContext) throws CommandException;
    /**
     * Returns the slash command name handled by this command.
     *
     * @return slash command name
     */
    String getCommand();
}
