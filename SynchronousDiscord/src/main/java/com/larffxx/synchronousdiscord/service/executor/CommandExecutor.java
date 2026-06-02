package com.larffxx.synchronousdiscord.service.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.exception.interaction.TelegramSlashInteractionException;
import com.larffxx.synchronousdiscord.domain.constants.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.service.controller.slashcommand.Command;
import com.larffxx.synchronousdiscord.service.registry.SlashCommandRegistry;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommandExecutor {
    private final SlashCommandRegistry slashCommandRegistry;
    private final CommandVerifier commandVerifier;

    public CommandExecutor(SlashCommandRegistry slashCommandRegistry, CommandVerifier commandVerifier) {
        this.slashCommandRegistry = slashCommandRegistry;
        this.commandVerifier = commandVerifier;
    }

    public void execute(SlashCommandInteractionEvent t) throws DiscordSlashInteractionException {
        t.deferReply().queue();
        commandVerifier.verifyCommand(t);
        Command command = slashCommandRegistry.getCommand(t.getInteraction().getName());

        command.execute(t);
    }

    public void execute(JsonNode data) throws TelegramSlashInteractionException {
        String strCommand = data.findValue(CommandConstants.COMMAND_VALUE).asText();

        commandVerifier.verifyCommand(data);
        Command command = slashCommandRegistry.getCommand(strCommand);

        command.execute(data);
    }
}
