package com.larffxx.synchronoustelegram.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.domain.record.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordCommandPayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import com.larffxx.synchronoustelegram.service.registry.CommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final TelegramClientCommandExecutorService telegramClientCommandExecutorService;
    private final DiscordCommandPayloadParser discordCommandPayloadParser;

    public DiscordCommandRouteService(TelegramClientCommandExecutorService telegramClientCommandExecutorService, DiscordCommandPayloadParser discordCommandPayloadParser) {
        this.telegramClientCommandExecutorService = telegramClientCommandExecutorService;
        this.discordCommandPayloadParser = discordCommandPayloadParser;
    }

    public void send(JsonNode data){
        DiscordPayload payload = discordCommandPayloadParser.parseDiscordCommand(data);

        telegramClientCommandExecutorService.execute(payload);
    }

    //TODO: route methods
    public void send(CommandContext commandContext){

    }

    public void send(MessageContext messageContext){

    }
}
