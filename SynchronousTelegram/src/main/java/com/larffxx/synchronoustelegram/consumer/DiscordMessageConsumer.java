package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.consumer.utility.DiscordMessageSender;
import com.larffxx.synchronoustelegram.exception.TelegramException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public class DiscordMessageConsumer {

    private final DiscordMessageSender discordMessageSender;

    public DiscordMessageConsumer(DiscordMessageSender discordMessageSender) {
        this.discordMessageSender = discordMessageSender;
    }


    @KafkaListener(topics = {"${dMTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String message) {
        try {
            JsonNode data = (new ObjectMapper()).readTree(message);
            discordMessageSender.sendTextMessageToTelegramChannel(data);
        } catch (JsonProcessingException | TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
