package com.larffxx.synchronousdiscord.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.routeservice.TelegramMessageRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class TelegramMessageConsumer {
    private final TelegramMessageRouteService telegramMessageRouteService;

    public TelegramMessageConsumer(TelegramMessageRouteService telegramMessageRouteService) {
        this.telegramMessageRouteService = telegramMessageRouteService;
    }

    @KafkaListener(topics = "${tMTopic}", groupId = "${groupId}")
    public void listener(@Payload String message) {
        JsonNode data = null;
        try {
            data = new ObjectMapper().readTree(message);

            telegramMessageRouteService.send(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
