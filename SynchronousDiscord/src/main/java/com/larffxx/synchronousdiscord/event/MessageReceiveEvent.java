package com.larffxx.synchronousdiscord.event;

import com.larffxx.synchronousdiscord.infrastructure.producer.DiscordPayloadProducer;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MessageReceiveEvent implements Event<MessageReceivedEvent> {
    private final DiscordPayloadProducer discordPayloadProducer;
    private final EventReceiver eventReceiver;

    public MessageReceiveEvent(DiscordPayloadProducer discordPayloadProducer, EventReceiver eventReceiver) {
        this.discordPayloadProducer = discordPayloadProducer;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        eventReceiver.setTextChannel(event.getChannel().asTextChannel());
        if (!event.getAuthor().isBot()) {
            discordPayloadProducer.send(event);
        }
    }

    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
