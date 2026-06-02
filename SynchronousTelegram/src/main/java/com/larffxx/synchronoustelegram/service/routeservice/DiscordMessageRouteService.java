package com.larffxx.synchronoustelegram.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordMessagePayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;

import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcher;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final MessageDispatcher messageDispatcher;
    private final DiscordMessagePayloadParser discordMessagePayloadParser;
    private final TextMessageService textMessageService;

    public DiscordMessageRouteService(MessageDispatcher messageDispatcher, DiscordMessagePayloadParser discordMessagePayloadParser, TextMessageService textMessageService) {
        this.messageDispatcher = messageDispatcher;
        this.discordMessagePayloadParser = discordMessagePayloadParser;
        this.textMessageService = textMessageService;
    }

    public void send(JsonNode data){
        DiscordPayload discordPayload = discordMessagePayloadParser.parseDiscordMessage(data);

        messageDispatcher.dispatch(discordPayload);
    }
}
