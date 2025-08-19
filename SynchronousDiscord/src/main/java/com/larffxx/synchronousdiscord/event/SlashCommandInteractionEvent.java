package com.larffxx.synchronousdiscord.event;

import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.producer.DiscordCommandProducer;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SlashCommandInteractionEvent implements Event<net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent> {
    private final DiscordCommandProducer discordCommandProducer;
    private final CommandExecutor commandExecutor;
    private final EventReceiver eventReceiver;

    public SlashCommandInteractionEvent(DiscordCommandProducer discordCommandProducer, CommandExecutor commandExecutor, EventReceiver eventReceiver1) {
        this.discordCommandProducer = discordCommandProducer;
        this.commandExecutor = commandExecutor;
        this.eventReceiver = eventReceiver1;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent event) {
        eventReceiver.setTextChannel(event.getChannel().asTextChannel());
        try {
            commandExecutor.execute(event);
        } catch (CommandException e) {
            event.reply(e.getMessage()).queue();
        }
        discordCommandProducer.send(event);
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent.class;
    }
}
