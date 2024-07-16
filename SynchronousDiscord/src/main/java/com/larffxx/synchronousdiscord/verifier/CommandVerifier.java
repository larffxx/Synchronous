package com.larffxx.synchronousdiscord.verifier;


import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Getter
@Setter
public class CommandVerifier {
    private final CommandPreProcessor commandPreProcessor;
    private final EventReceiver eventReceiver;

    public CommandVerifier(CommandPreProcessor commandPreProcessor, EventReceiver eventReceiver) {
        this.commandPreProcessor = commandPreProcessor;
        this.eventReceiver = eventReceiver;
    }

    public void commandVerifier(SlashCommandInteractionEvent t) throws CommandException {
        List<OptionMapping> options = t.getInteraction().getOptions();

        if (t.getOptions().isEmpty() && !options.isEmpty()) {
            throw new CommandException(InfExcMessages.VALUES_NOT_PROVIDED_ERROR);
        }

        String option = commandPreProcessor.getCommand(t.getInteraction().getName()).getOption();
        OptionMapping optionMapping = t.getOption(option);


        eventReceiver.setMessageChannel(t.getMessageChannel());
        eventReceiver.setOptionMappings(options);

        if (optionMapping != null && !optionMapping.getType().equals(optionMapping.getType())) {
            throw new CommandException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
        }

        if(optionMapping != null && optionMapping.getType().equals(OptionType.INTEGER) && optionMapping.getAsInt() < 0){
            throw new CommandException(InfExcMessages.REQUIRED_VALUE_NOT_PROVIDED);
        }
    }
}
