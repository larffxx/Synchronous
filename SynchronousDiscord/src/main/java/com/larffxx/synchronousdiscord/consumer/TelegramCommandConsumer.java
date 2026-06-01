package com.larffxx.synchronousdiscord.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.util.verifier.CommandVerifier;
import com.larffxx.synchronousdiscord.application.routeservice.TelegramCommandRouteService;
import com.larffxx.synchronousdiscord.preprocessor.SlashCommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class TelegramCommandConsumer {
    private final SlashCommandPreProcessor preProcessor;
    private final TelegramCommandRouteService telegramCommandRouteService;
    private final CommandVerifier commandVerifier;

    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService,  SlashCommandPreProcessor preProcessor, CommandVerifier commandVerifier) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.preProcessor = preProcessor;
        this.commandVerifier = commandVerifier;
    }

    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data;
        try {
            data = new ObjectMapper().readTree(command);

            telegramCommandRouteService.send(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
