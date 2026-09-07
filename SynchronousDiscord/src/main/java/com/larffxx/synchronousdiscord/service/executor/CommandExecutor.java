package com.larffxx.synchronousdiscord.service.executor;

import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.TelegramSlashInteractionException;
import com.larffxx.synchronousdiscord.service.controller.slashcommand.Command;
import com.larffxx.synchronousdiscord.service.registry.SlashCommandRegistry;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

/**
 * Verifies and dispatches slash commands from Discord and Telegram.
 */
@Component
@Getter
@Setter
public class CommandExecutor {
    /**
     * Registry used to look up slash command handlers.
     */
    private final SlashCommandRegistry slashCommandRegistry;
    /**
     * Verifier used to validate command options before execution.
     */
    private final CommandVerifier commandVerifier;

    /**
     * Creates a command executor.
     *
     * @param slashCommandRegistry registry of slash commands
     * @param commandVerifier verifier for command options
     */
    public CommandExecutor(SlashCommandRegistry slashCommandRegistry, CommandVerifier commandVerifier) {
        this.slashCommandRegistry = slashCommandRegistry;
        this.commandVerifier = commandVerifier;
    }

    /**
     * Verifies and executes a Discord slash command interaction.
     *
     * @param t Discord slash command interaction event
     * @throws DiscordSlashInteractionException when verification or execution fails
     */
    public void execute(SlashCommandInteractionEvent t) throws DiscordSlashInteractionException {
        t.deferReply().queue();
        commandVerifier.verifyCommand(t);
        Command command = slashCommandRegistry.getSender(t.getInteraction().getName());

        command.execute(t);
    }

    /**
     * Verifies and executes a Telegram command on the Discord side.
     *
     * @param telegramCommandContext Telegram command context to execute
     * @throws TelegramSlashInteractionException when verification or execution fails
     */
    public void execute(TelegramCommandContext telegramCommandContext) throws TelegramSlashInteractionException {
        commandVerifier.verifyCommand(telegramCommandContext);
        Command command = slashCommandRegistry.getSender(telegramCommandContext.command());
        if (command == null) {
            return;
        }

        command.execute(telegramCommandContext);
    }
}
