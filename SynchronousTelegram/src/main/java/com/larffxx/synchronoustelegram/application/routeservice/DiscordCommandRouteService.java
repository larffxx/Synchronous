package com.larffxx.synchronoustelegram.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
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
    private final UpdateReceiver updateHolder;

    public DiscordCommandRouteService(DiscordToTelegramCommandService discordToTelegramCommandService, DiscordCommandPayloadParser discordCommandPayloadParser, CommandPreProcessor preProcessor, UpdateReceiver updateReceiver) {
        this.discordToTelegramCommandService = discordToTelegramCommandService;
        this.discordCommandPayloadParser = discordCommandPayloadParser;
        this.preProcessor = preProcessor;
        this.updateHolder = updateReceiver;
    }

    public void send(JsonNode data){
        DiscordPayload payload = discordCommandPayloadParser.parseDiscordCommand(data);

        discordToTelegramCommandService.execute(payload);
    }
}
