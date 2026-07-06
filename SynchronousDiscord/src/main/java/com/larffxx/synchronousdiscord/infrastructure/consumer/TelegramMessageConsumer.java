package com.larffxx.synchronousdiscord.infrastructure.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.exception.consume.CommandConsumingException;
import com.larffxx.synchronousdiscord.infrastructure.mapper.JsonNodeToTelegramMessageContextContextMapper;
import com.larffxx.synchronousdiscord.service.routeservice.TelegramMessageRouteService;
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
    private final JsonNodeToTelegramMessageContextContextMapper jsonNodeToTelegramMessageContextMapper;

    public TelegramMessageConsumer(TelegramMessageRouteService telegramMessageRouteService, JsonNodeToTelegramMessageContextContextMapper jsonNodeToTelegramMessageContextMapper) {
        this.telegramMessageRouteService = telegramMessageRouteService;
        this.jsonNodeToTelegramMessageContextMapper = jsonNodeToTelegramMessageContextMapper;
    }

    //TODO: Consuming with mapping context
    @KafkaListener(topics = "${tMTopic}", groupId = "${groupId}")
    public void listener(@Payload String message) {
        JsonNode data;
        try {
            data = new ObjectMapper().readTree(message);

            telegramMessageRouteService.send(jsonNodeToTelegramMessageContextMapper.toContext(data));
        } catch (JsonProcessingException e) {
            throw new CommandConsumingException(InfExcMessages.MESSAGE_CONSUMING_EXCEPTION);
        }
    }
}
