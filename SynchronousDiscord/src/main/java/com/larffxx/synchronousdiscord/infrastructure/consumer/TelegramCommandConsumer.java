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


@Component
@Getter
@Setter
public class TelegramCommandConsumer {
    private final TelegramCommandRouteService telegramCommandRouteService;
    private final JsonNodeToTelegramCommandContextContextMapper jsonNodeToTelegramCommandContextContextMapper;

    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService, JsonNodeToTelegramCommandContextContextMapper jsonNodeToTelegramCommandContextContextMapper) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.jsonNodeToTelegramCommandContextContextMapper = jsonNodeToTelegramCommandContextContextMapper;
    }

    //TODO: Consuming with mapping context
    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data;
        try {
            data = new ObjectMapper().readTree(command);

            telegramCommandRouteService.send(jsonNodeToTelegramCommandContextContextMapper.toContext(data));
        } catch (JsonProcessingException e) {
            throw new CommandConsumingException(InfExcMessages.COMMAND_CONSUMING_EXCEPTION);
        }
    }
}
