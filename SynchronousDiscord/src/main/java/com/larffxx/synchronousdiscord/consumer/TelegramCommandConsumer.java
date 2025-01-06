package com.larffxx.synchronousdiscord.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.verifier.CommandVerifier;
import com.larffxx.synchronousdiscord.routeservices.TelegramCommandRouteService;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.SlashCommandPreProcessor;
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
    private final GuildProfileDAO guildProfileDAO;
    private final TelegramCommandRouteService telegramCommandRouteService;
    private final CommandVerifier commandVerifier;

    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService, GuildProfileDAO guildProfileDAO, SlashCommandPreProcessor preProcessor, CommandVerifier commandVerifier) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.guildProfileDAO = guildProfileDAO;
        this.preProcessor = preProcessor;
        this.commandVerifier = commandVerifier;
    }

    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data = null;
        try {
            data = new ObjectMapper().readTree(command);

            telegramCommandRouteService.send(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
