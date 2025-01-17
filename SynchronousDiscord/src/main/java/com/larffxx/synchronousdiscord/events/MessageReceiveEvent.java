package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.producer.DiscordMessageProducer;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MessageReceiveEvent implements Event<MessageReceivedEvent> {
    private final DiscordMessageProducer discordMessageProducer;
    private final EventReceiver eventReceiver;

    public MessageReceiveEvent(DiscordMessageProducer discordMessageProducer, EventReceiver eventReceiver) {
        this.discordMessageProducer = discordMessageProducer;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        eventReceiver.setTextChannel(event.getChannel().asTextChannel());
        if (!event.getAuthor().isBot()) {
            if (event.getMessage().getAttachments().isEmpty()) {
                discordMessageProducer.send(event);
            } else {
                discordMessageProducer.sendWithAttachment(event);
            }
        }
    }

    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
