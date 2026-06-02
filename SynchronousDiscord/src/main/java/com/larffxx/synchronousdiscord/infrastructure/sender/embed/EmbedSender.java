package com.larffxx.synchronousdiscord.infrastructure.sender.embed;

import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
public class EmbedSender implements Sender<EmbedBuilder> {
    private final EventReceiver eventReceiver;

    public EmbedSender(EventReceiver eventReceiver) {
        this.eventReceiver = eventReceiver;
    }

    @Override
    public void send(EmbedBuilder eb) {
        eventReceiver.getTextChannel().sendMessageEmbeds(eb.build()).queue();
    }

    @Override
    public String getSender() {
        return "embedSender";
    }
}
