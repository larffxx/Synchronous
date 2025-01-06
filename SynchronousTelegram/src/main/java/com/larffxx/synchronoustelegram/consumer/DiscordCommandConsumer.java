package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.exception.TelegramException;
import com.larffxx.synchronoustelegram.routeservice.DiscordCommandRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DiscordCommandConsumer {
    private final DiscordCommandRouteService discordCommandRouteService;

    public DiscordCommandConsumer(DiscordCommandRouteService discordCommandRouteService) {
        this.discordCommandRouteService = discordCommandRouteService;
    }

    @KafkaListener(topics = {"${dCTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String command){
        try {
            JsonNode data = new ObjectMapper().readTree(command);

            discordCommandRouteService.send(data);
        } catch (JsonProcessingException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
