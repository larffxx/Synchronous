package com.larffxx.synchronousdiscord.infrastructure.discord.event;

import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MessageReceiveEvent implements Event<MessageReceivedEvent> {
    private final DiscordPayloadProducer discordPayloadProducer;
    private final EventContext eventContext;

    public MessageReceiveEvent(DiscordPayloadProducer discordPayloadProducer, EventContext eventContext) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.eventContext = eventContext;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        eventContext.setTextChannel(event.getChannel().asTextChannel());
        if (!event.getAuthor().isBot()) {
            discordPayloadProducer.send(event);
        }
    }

    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
