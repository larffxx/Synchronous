package com.larffxx.synchronoustelegram.infrastructure.consumer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.infrastructure.mapper.ContextMapper;
import com.larffxx.synchronoustelegram.infrastructure.mapper.JsonNodeToMessageContextMapper;
import com.larffxx.synchronoustelegram.service.routeservice.DiscordMessageRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for message contexts coming from Discord.
 * Parses each record into a message context and routes it for delivery.
 */
@Component
@Getter
@Setter
public class DiscordMessageConsumer {
    /**
     * Shared thread-safe JSON parser.
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * Service that routes Discord message contexts for delivery.
     */
    private final DiscordMessageRouteService discordMessageRouteService;

    /**
     * Creates the consumer with its route service.
     * @param discordMessageRouteService service routing message contexts
     */
    public DiscordMessageConsumer(DiscordMessageRouteService discordMessageRouteService) {
        this.discordMessageRouteService = discordMessageRouteService;
    }

    /**
     * Listens to the Discord message topic and routes each received message.
     * @param message the raw message JSON payload
     * @throws RuntimeException if the payload cannot be parsed
     */
    @KafkaListener(topics = {"${dMTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String message) {
        try {
            JsonNode data = OBJECT_MAPPER.readTree(message);

            ContextMapper<MessageContext> contextMapper = new JsonNodeToMessageContextMapper();
            MessageContext context = contextMapper.mapToContext(data);

            discordMessageRouteService.send(context);
        } catch (JsonProcessingException e) {
            throw new TelegramException(e.getMessage(), e);
        }
    }
}
