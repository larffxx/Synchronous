package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.routeservice.DiscordMessageRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DiscordMessageConsumer {
    private final DiscordMessageRouteService discordMessageRouteService;

    public DiscordMessageConsumer(DiscordMessageRouteService discordMessageRouteService) {
        this.discordMessageRouteService = discordMessageRouteService;
    }

    @KafkaListener(topics = {"${dMTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String message) {
        try {
            JsonNode data = new ObjectMapper().readTree(message);

            discordMessageRouteService.send(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
