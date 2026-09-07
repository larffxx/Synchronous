package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
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
     * Creates a handler for received Discord messages.
     *
     * @param discordPayloadProducer producer for outgoing payloads
     * @param eventContext shared event context
     */
    public MessageReceiveEvent(DiscordPayloadProducer discordPayloadProducer, EventContext eventContext) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.eventContext = eventContext;
    }

    /**
     * Stores the channel and forwards the message when it is not from a bot.
     *
     * @param event message received event from JDA
     */
    @Override
    public void execute(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            eventContext.setTextChannel(event.getChannel().asTextChannel());
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
