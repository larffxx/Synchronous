package com.larffxx.synchronoustelegram.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.service.DiscordToTelegramMessageService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final DiscordToTelegramMessageService discordToTelegramMessageService;

    public DiscordMessageRouteService(DiscordToTelegramMessageService discordToTelegramMessageService) {
        this.discordToTelegramMessageService = discordToTelegramMessageService;
    }

    public void send(JsonNode data){
        discordToTelegramMessageService.sendMessageToTelegramChannel(data);
    }
}
