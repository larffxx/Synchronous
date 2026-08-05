package com.larffxx.synchronousdiscord.infrastructure.sender.embed;

import com.larffxx.synchronousdiscord.domain.context.EmbedContext;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import org.springframework.stereotype.Component;

@Component
public class EmbedSender implements Sender<EmbedContext> {
    private final EventContext eventContext;

    public EmbedSender(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public void send(EmbedContext embedContext) {
        eventContext.getTextChannel().sendMessageEmbeds(embedContext.embedBuilder().build()).queue();
    }

    @Override
    public String getSender() {
        return "embedSender";
    }
}
