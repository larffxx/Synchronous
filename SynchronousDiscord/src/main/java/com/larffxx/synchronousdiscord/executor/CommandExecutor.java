package com.larffxx.synchronousdiscord.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.verifier.CommandVerifier;
import com.larffxx.synchronousdiscord.controller.slashcommand.Command;
import com.larffxx.synchronousdiscord.preprocessor.SlashCommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommandExecutor {
    private final SlashCommandPreProcessor slashCommandPreProcessor;
    private final CommandVerifier commandVerifier;

    public CommandExecutor(SlashCommandPreProcessor slashCommandPreProcessor, CommandVerifier commandVerifier) {
        this.slashCommandPreProcessor = slashCommandPreProcessor;
        this.commandVerifier = commandVerifier;
    }

    public void execute(SlashCommandInteractionEvent t) throws CommandException {
        t.deferReply().queue();
        commandVerifier.verifyCommand(t);
        Command command = slashCommandPreProcessor.getCommand(t.getInteraction().getName());

        command.execute(t);
    }

    public void execute(JsonNode data) throws CommandException {
        String strCommand = data.findValue(CommandConstants.COMMAND_VALUE).asText();

        commandVerifier.verifyCommand(data);
        Command command = slashCommandPreProcessor.getCommand(strCommand);

        command.execute(data);
    }
}
