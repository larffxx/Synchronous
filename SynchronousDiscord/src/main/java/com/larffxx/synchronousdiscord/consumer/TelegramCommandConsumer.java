package com.larffxx.synchronousdiscord.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronousdiscord.dao.GuildProfileDAO;
import com.larffxx.synchronousdiscord.senders.TelegramCommandRouteService;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;


@Component
@Getter
@Setter
public class TelegramCommandConsumer {
    private final CommandPreProcessor preProcessor;
    private final GuildProfileDAO guildProfileDAO;
    private final TelegramCommandRouteService telegramCommandRouteService;

    public TelegramCommandConsumer(TelegramCommandRouteService telegramCommandRouteService, GuildProfileDAO guildProfileDAO, CommandPreProcessor preProcessor) {
        this.telegramCommandRouteService = telegramCommandRouteService;
        this.guildProfileDAO = guildProfileDAO;
        this.preProcessor = preProcessor;
    }

    @KafkaListener(topics = "${tCTopic}", groupId = "${groupId}")
    public void listener(@Payload String command) {
        JsonNode data = null;
        try {
            data = new ObjectMapper().readTree(command);
            Command com = preProcessor.getCommand(data.findValue("command").asText());
            com.execute(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        telegramCommandRouteService.send(data);
    }
}
