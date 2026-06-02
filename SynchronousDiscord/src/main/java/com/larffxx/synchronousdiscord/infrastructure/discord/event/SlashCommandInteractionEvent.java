package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.service.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SlashCommandInteractionEvent implements Event<net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent> {
    private final DiscordPayloadProducer discordPayloadProducer;
    private final CommandExecutor commandExecutor;
    private final EventReceiver eventReceiver;

    public SlashCommandInteractionEvent(DiscordPayloadProducer discordPayloadProducer, CommandExecutor commandExecutor, EventReceiver eventReceiver) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.commandExecutor = commandExecutor;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void execute(net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent event) {
        eventReceiver.setTextChannel(event.getChannel().asTextChannel());
        try {
            commandExecutor.execute(event);
        } catch (DiscordSlashInteractionException e) {
            event.reply(e.getMessage()).queue();
        }
        discordPayloadProducer.send(event);
    }

    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent.class;
    }
}
