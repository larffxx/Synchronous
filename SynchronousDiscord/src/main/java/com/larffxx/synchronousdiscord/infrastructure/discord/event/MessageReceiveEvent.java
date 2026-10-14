package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.service.CoolnessService;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

/**
 * Forwards incoming Discord messages to the synchronization pipeline.
 */
@Component
@Getter
@Setter
public class MessageReceiveEvent implements Event<MessageReceivedEvent> {
    /**
     * Producer used to forward received message payloads.
     */
    private final DiscordPayloadProducer discordPayloadProducer;
    /**
     * Shared context holding the current text channel.
     */
    private final EventContext eventContext;
    /**
     * Service scoring chat messages for coolness.
     */
    private final CoolnessService coolnessService;
    /**
     * Repository used to determine whether message scoring is enabled for a guild.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a handler for received Discord messages.
     *
     * @param discordPayloadProducer producer for outgoing payloads
     * @param eventContext shared event context
     * @param coolnessService service scoring chat messages
     */
    public MessageReceiveEvent(DiscordPayloadProducer discordPayloadProducer, EventContext eventContext, CoolnessService coolnessService, ServersConnectRepository serversConnectRepository) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.eventContext = eventContext;
        this.coolnessService = coolnessService;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Stores the channel and forwards the message when it is not from a bot.
     *
     * @param event message received event from JDA
     */
    @Override
    public void execute(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            if (event.getGuild() == null || !serversConnectRepository.existsByDiscordGuild(event.getGuild().getId())) {
                return;
            }
            eventContext.setTextChannel(event.getChannel().asTextChannel());
            coolnessService.handleMessage(event);
            discordPayloadProducer.send(event);
        }
    }

    /**
     * Returns the message received event class.
     *
     * @return message received event class
     */
    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
