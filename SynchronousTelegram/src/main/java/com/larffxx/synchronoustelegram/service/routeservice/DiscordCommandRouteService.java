package com.larffxx.synchronoustelegram.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordCommandPayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutor;
import com.larffxx.synchronoustelegram.service.registry.CommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final TelegramClientCommandExecutor telegramClientCommandExecutor;
    private final DiscordCommandPayloadParser discordCommandPayloadParser;
    private final CommandRegistry preProcessor;
    private final UpdateReceiver updateHolder;

    public DiscordCommandRouteService(TelegramClientCommandExecutor telegramClientCommandExecutor, DiscordCommandPayloadParser discordCommandPayloadParser, CommandRegistry preProcessor, UpdateReceiver updateReceiver) {
        this.telegramClientCommandExecutor = telegramClientCommandExecutor;
        this.discordCommandPayloadParser = discordCommandPayloadParser;
        this.preProcessor = preProcessor;
        this.updateHolder = updateReceiver;
    }

    public void send(JsonNode data){
        DiscordPayload payload = discordCommandPayloadParser.parseDiscordCommand(data);

        telegramClientCommandExecutor.execute(payload);
    }
}
