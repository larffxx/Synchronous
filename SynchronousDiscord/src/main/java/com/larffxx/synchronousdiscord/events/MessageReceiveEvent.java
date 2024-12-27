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
public class MessageReceiveEvent implements Event<MessageReceivedEvent> {
    private final CommandListener commandListener;
    private final EventReceiver eventReceiver;

    public MessageReceiveEvent(CommandListener commandListener, EventReceiver eventReceiver) {
        this.commandListener = commandListener;
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void execute(MessageReceivedEvent event) {
        eventReceiver.setTextChannel(event.getChannel().asTextChannel());
        if (!event.getAuthor().isBot()) {
            if (event.getMessage().getAttachments().isEmpty()) {
                commandListener.getDiscordMessageProducer().send(event);
            } else {
                commandListener.getDiscordMessageProducer().sendWithAttachment(event);
            }
        }
    }

    @Override
    public Class getEvent() {
        return MessageReceivedEvent.class;
    }

}
