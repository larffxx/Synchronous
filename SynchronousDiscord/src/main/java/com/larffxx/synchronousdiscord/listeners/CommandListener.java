package com.larffxx.synchronousdiscord.listeners;

import com.larffxx.synchronousdiscord.senders.EmbedSender;
import com.larffxx.synchronousdiscord.producer.DiscordMessageProducer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CommandListener {
    @Value("${clientId}")
    private String clientId;
    @Value("${clientSecret}")
    private String clientSecret;
    private final EmbedSender embedSender;
    private final DiscordMessageProducer discordMessageProducer;

    public CommandListener(EmbedSender embedSender, DiscordMessageProducer discordMessageProducer) {
        this.embedSender = embedSender;
        this.discordMessageProducer = discordMessageProducer;
    }
}
