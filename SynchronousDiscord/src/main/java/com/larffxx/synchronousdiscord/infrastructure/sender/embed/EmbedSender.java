package com.larffxx.synchronousdiscord.infrastructure.sender.embed;

import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmbedSender implements Sender<EmbedBuilder> {
    private final EventContext eventContext;

    public EmbedSender(EventContext eventContext) {
        this.eventContext = eventContext;
    }

    @Override
    public void send(EmbedBuilder eb) {
        eventContext.getTextChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getSender() {
        return "embedSender";
    }
}
