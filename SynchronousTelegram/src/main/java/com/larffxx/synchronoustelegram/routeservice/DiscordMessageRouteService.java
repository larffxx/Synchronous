package com.larffxx.synchronoustelegram.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.sender.DiscordMessageSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final DiscordMessageSender discordMessageSender;

    public DiscordMessageRouteService(DiscordMessageSender discordMessageSender) {
        this.discordMessageSender = discordMessageSender;
    }

    public void send(JsonNode data){
        try {
            discordMessageSender.sendTextMessageToTelegramChannel(data);
        } catch (TelegramApiException e) {
            //TODO custom exception
            throw new RuntimeException(e);
        }
    }
}
