package com.larffxx.synchronousdiscord.verifier;


import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.preprocessor.SlashCommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter
@Setter
public class CommandVerifier {
    private final SlashCommandPreProcessor slashCommandPreProcessor;
    private final EventReceiver eventReceiver;

    public CommandVerifier(SlashCommandPreProcessor slashCommandPreProcessor, EventReceiver eventReceiver) {
        this.slashCommandPreProcessor = slashCommandPreProcessor;
        this.eventReceiver = eventReceiver;
    }

    public void commandVerifier(SlashCommandInteractionEvent t) throws CommandException {
        List<Command.Option> commandOptions = t.getGuild().retrieveCommandById(t.getCommandId()).complete().getOptions();
        List<OptionMapping> options = t.getInteraction().getOptions();

        if (commandOptions.size() > t.getOptions().size()) {
            throw new CommandException(InfExcMessages.VALUES_NOT_PROVIDED_ERROR);
        }

        eventReceiver.setMessageChannel(t.getMessageChannel());
        eventReceiver.setOptionMappings(options);

        for (OptionMapping option : options) {
            if (option != null && !option.getType().equals(option.getType())) {
                throw new CommandException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
            }

            if (option != null && option.getType().equals(OptionType.INTEGER) && option.getAsInt() < 0) {
                throw new CommandException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
            }
        }
    }
}
