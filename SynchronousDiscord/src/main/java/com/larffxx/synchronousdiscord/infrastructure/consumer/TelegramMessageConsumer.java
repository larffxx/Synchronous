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

/**
 * Telegram Message Consumer class.
 */
@Component
@Getter
@Setter
public class TelegramMessageConsumer {
    /**
     * Shared thread-safe JSON parser.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * The telegram message route service.
     */
    private final TelegramMessageRouteService telegramMessageRouteService;
    /**
     * The json node to telegram message context mapper.
     */
    private final JsonNodeToTelegramMessageContextContextMapper jsonNodeToTelegramMessageContextMapper;

    /**
     * Creates a new TelegramMessageConsumer.
     * @param telegramMessageRouteService the telegram message route service.
     * @param jsonNodeToTelegramMessageContextMapper the json node to telegram message context mapper.
     */
    public TelegramMessageConsumer(TelegramMessageRouteService telegramMessageRouteService, JsonNodeToTelegramMessageContextContextMapper jsonNodeToTelegramMessageContextMapper) {
        this.telegramMessageRouteService = telegramMessageRouteService;
        this.jsonNodeToTelegramMessageContextMapper = jsonNodeToTelegramMessageContextMapper;
    }

    /**
     * Listens for incoming Kafka messages.
     * @param message the message.
     */
    @KafkaListener(topics = "${tMTopic}", groupId = "${groupId}")
    public void listener(@Payload String message) {
        JsonNode data;
        try {
            data = OBJECT_MAPPER.readTree(message);

            telegramMessageRouteService.send(jsonNodeToTelegramMessageContextMapper.toContext(data));
        } catch (JsonProcessingException e) {
            throw new CommandConsumingException(InfExcMessages.MESSAGE_CONSUMING_EXCEPTION, e);
        }
    }
}
