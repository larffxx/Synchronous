package com.larffxx.synchronousdiscord.listeners;

import com.larffxx.synchronousdiscord.senders.EmbedSender;
import com.larffxx.synchronousdiscord.senders.SendKafkaMessage;
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
    private final SendKafkaMessage sendKafkaMessage;

    public CommandListener(EmbedSender embedSender, SendKafkaMessage sendKafkaMessage) {
        this.embedSender = embedSender;
        this.sendKafkaMessage = sendKafkaMessage;
    }
}
