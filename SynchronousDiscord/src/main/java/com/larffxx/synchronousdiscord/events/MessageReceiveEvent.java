package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MessageReceiveEvent extends Event<MessageReceivedEvent> {
    private final CommandListener commandListener;

    public MessageReceiveEvent(EventReceiver eventReceiver, CommandListener commandListener) {
        super(eventReceiver);
        this.commandListener = commandListener;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        getEventReceiver().setTextChannel(event.getChannel().asTextChannel());
        if (!event.getAuthor().isBot()) {
            if (event.getMessage().getAttachments().isEmpty()) {
                commandListener.getDiscordMessageProducer().send(event);
            } else {
                commandListener.getDiscordMessageProducer().send(event, event.getMessage().getAttachments().get(0).getUrl());
            }
        }
    }

    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
