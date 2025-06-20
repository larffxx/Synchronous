package com.larffxx.synchronoustelegram.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.domain.service.DiscordToTelegramCommandService;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordCommandPayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final DiscordToTelegramCommandService discordToTelegramCommandService;
    private final DiscordCommandPayloadParser discordCommandPayloadParser;
    private final CommandPreProcessor preProcessor;
    private final UpdateHandler updateHolder;

    public DiscordCommandRouteService(DiscordToTelegramCommandService discordToTelegramCommandService, DiscordCommandPayloadParser discordCommandPayloadParser, CommandPreProcessor preProcessor, UpdateHandler updateHandler) {
        this.discordToTelegramCommandService = discordToTelegramCommandService;
        this.discordCommandPayloadParser = discordCommandPayloadParser;
        this.preProcessor = preProcessor;
        this.updateHolder = updateHandler;
    }

    public void send(JsonNode data){
        DiscordPayload payload = discordCommandPayloadParser.parseDiscordCommand(data);

        discordToTelegramCommandService.execute(payload);
    }
}
