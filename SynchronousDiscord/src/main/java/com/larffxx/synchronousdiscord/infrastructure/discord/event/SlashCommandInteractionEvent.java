package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.domain.exception.interaction.DiscordSlashInteractionException;
import com.larffxx.synchronousdiscord.service.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Handles Discord slash command interaction events.
 */
@Component
@Getter
@Setter
public class SlashCommandInteractionEvent implements Event<net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent> {
    /**
     * Producer used to forward slash command payloads.
     */
    private final DiscordPayloadProducer discordPayloadProducer;
    /**
     * Executor used to run the invoked slash command.
     */
    private final CommandExecutor commandExecutor;
    /**
     * Shared context holding the current text channel.
     */
    private final EventContext eventContext;

    /**
     * Creates a handler for slash command interaction events.
     *
     * @param discordPayloadProducer producer for outgoing payloads
     * @param commandExecutor executor for slash commands
     * @param eventContext shared event context
     */
    public SlashCommandInteractionEvent(DiscordPayloadProducer discordPayloadProducer, CommandExecutor commandExecutor, EventContext eventContext) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.commandExecutor = commandExecutor;
        this.eventContext = eventContext;
    }

    /**
     * Executes the slash command and forwards the event payload.
     *
     * @param event slash command interaction event from JDA
     */
    @Override
    public void execute(net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent event) {
        eventContext.setTextChannel(event.getChannel().asTextChannel());
        try {
            commandExecutor.execute(event);
        } catch (DiscordSlashInteractionException e) {
            event.reply(e.getMessage()).queue();
        }
        discordPayloadProducer.send(event);
    }

    /**
     * Returns the JDA slash command interaction event class.
     *
     * @return slash command interaction event class
     */
    @Override
    public Class getEvent() {
        return net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent.class;
    }
}
