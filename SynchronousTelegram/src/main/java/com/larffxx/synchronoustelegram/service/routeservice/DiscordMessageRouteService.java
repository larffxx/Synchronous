package com.larffxx.synchronoustelegram.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordMessagePayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;

import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final MessageDispatcherService messageDispatcherService;
    private final DiscordMessagePayloadParser discordMessagePayloadParser;
    private final TextMessageService textMessageService;

    public DiscordMessageRouteService(MessageDispatcherService messageDispatcherService, DiscordMessagePayloadParser discordMessagePayloadParser, TextMessageService textMessageService) {
        this.messageDispatcherService = messageDispatcherService;
        this.discordMessagePayloadParser = discordMessagePayloadParser;
        this.textMessageService = textMessageService;
    }

    public void send(JsonNode data){
        DiscordPayload discordPayload = discordMessagePayloadParser.parseDiscordMessage(data);

        messageDispatcherService.dispatch(discordPayload);
    }
}
