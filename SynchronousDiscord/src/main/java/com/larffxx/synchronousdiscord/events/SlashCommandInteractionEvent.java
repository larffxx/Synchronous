package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SlashCommandInteractionEvent extends Event<net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent> {
    private final CommandPreProcessor commandPreProcessor;

    public SlashCommandInteractionEvent(EventReceiver eventReceiver, CommandPreProcessor commandPreProcessor) {
        super(eventReceiver);
        this.commandPreProcessor = commandPreProcessor;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent event) {
        getEventReceiver().setTextChannel(event.getChannel().asTextChannel());
        Command command = commandPreProcessor.getCommand(event.getInteraction().getName());
        command.execute(event);
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent.class;
    }
}
