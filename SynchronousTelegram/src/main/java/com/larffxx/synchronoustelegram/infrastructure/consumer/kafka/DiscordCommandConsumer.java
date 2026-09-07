package com.larffxx.synchronoustelegram.infrastructure.consumer.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.mapper.ContextMapper;
import com.larffxx.synchronoustelegram.infrastructure.mapper.JsonNodeToCommandContextMapper;
import com.larffxx.synchronoustelegram.service.routeservice.DiscordCommandRouteService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for command contexts coming from Discord.
 * Parses each record into a command context and routes it for execution.
 */
@Component
@Getter
@Setter
public class DiscordCommandConsumer {
    /**
     * Service that routes Discord command contexts for execution.
     */
    private final DiscordCommandRouteService discordCommandRouteService;

    /**
     * Creates the consumer with its route service.
     * @param discordCommandRouteService service routing command contexts
     */
    public DiscordCommandConsumer(DiscordCommandRouteService discordCommandRouteService) {
        this.discordCommandRouteService = discordCommandRouteService;
    }

    /**
     * Listens to the Discord command topic and routes each received command.
     * @param command the raw command JSON payload
     * @throws TelegramException if the payload cannot be parsed
     */
    @KafkaListener(topics = {"${dCTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String command){
        try {
            JsonNode data = new ObjectMapper().readTree(command);

            ContextMapper<CommandContext> contextMapper = new JsonNodeToCommandContextMapper();
            CommandContext context = contextMapper.mapToContext(data);

            discordCommandRouteService.send(context);
        } catch (JsonProcessingException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
