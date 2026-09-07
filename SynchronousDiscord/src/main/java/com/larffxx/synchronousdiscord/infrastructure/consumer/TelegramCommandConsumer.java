package com.larffxx.synchronousdiscord.infrastructure.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.exception.consume.CommandConsumingException;
import com.larffxx.synchronousdiscord.infrastructure.mapper.JsonNodeToTelegramCommandContextContextMapper;
import com.larffxx.synchronousdiscord.service.routeservice.TelegramCommandRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Telegram Command Consumer class.
 */
@Component
@Getter
@Setter
public class TelegramCommandConsumer {
    /**
     * Shared thread-safe JSON parser.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * The telegram command route service.
     */
    private final TelegramCommandRouteService telegramCommandRouteService;
    /**
     * The json node to telegram command context context mapper.
     */
    private final JsonNodeToTelegramCommandContextContextMapper jsonNodeToTelegramCommandContextContextMapper;

    /**
     * Creates a new TelegramCommandConsumer.
     * @param telegramCommandRouteService the telegram command route service.
     * @param jsonNodeToTelegramCommandContextContextMapper the json node to telegram command context context mapper.
     */
    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService, JsonNodeToTelegramCommandContextContextMapper jsonNodeToTelegramCommandContextContextMapper) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.jsonNodeToTelegramCommandContextContextMapper = jsonNodeToTelegramCommandContextContextMapper;
    }

    /**
     * Listens for incoming Kafka messages.
     * @param command the command.
     */
    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data;
        try {
            data = OBJECT_MAPPER.readTree(command);

            telegramCommandRouteService.send(jsonNodeToTelegramCommandContextContextMapper.toContext(data));
        } catch (JsonProcessingException e) {
            throw new CommandConsumingException(InfExcMessages.COMMAND_CONSUMING_EXCEPTION, e);
        }
    }
}
