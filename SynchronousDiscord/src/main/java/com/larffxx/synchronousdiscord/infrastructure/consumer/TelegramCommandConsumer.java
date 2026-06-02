package com.larffxx.synchronousdiscord.infrastructure.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.domain.constants.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.exception.consume.CommandConsumingException;
import com.larffxx.synchronousdiscord.service.executor.CommandVerifier;
import com.larffxx.synchronousdiscord.service.routeservice.TelegramCommandRouteService;
import com.larffxx.synchronousdiscord.service.registry.SlashCommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class TelegramCommandConsumer {
    private final SlashCommandRegistry slashCommandRegistry;
    private final TelegramCommandRouteService telegramCommandRouteService;
    private final CommandVerifier commandVerifier;

    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService, SlashCommandRegistry slashCommandRegistry, CommandVerifier commandVerifier) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.slashCommandRegistry = slashCommandRegistry;
        this.commandVerifier = commandVerifier;
    }

    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data;
        try {
            data = new ObjectMapper().readTree(command);

            telegramCommandRouteService.send(data);
        } catch (JsonProcessingException e) {
            throw new CommandConsumingException(InfExcMessages.COMMAND_CONSUMING_EXCEPTION);
        }
    }
}
