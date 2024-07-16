package com.larffxx.synchronousdiscord.executor;

import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.verifier.CommandVerifier;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommandExecutor {
    private final CommandPreProcessor commandPreProcessor;
    private final CommandVerifier commandVerifier;

    public CommandExecutor(CommandPreProcessor commandPreProcessor, CommandVerifier commandVerifier) {
        this.commandPreProcessor = commandPreProcessor;
        this.commandVerifier = commandVerifier;
    }

    public void execute(SlashCommandInteractionEvent t) throws CommandException {
        commandVerifier.commandVerifier(t);
        Command command = commandPreProcessor.getCommand(t.getInteraction().getName());

        command.execute(t);
    }
}
