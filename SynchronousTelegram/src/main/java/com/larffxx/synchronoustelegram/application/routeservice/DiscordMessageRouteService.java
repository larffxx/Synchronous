package com.larffxx.synchronoustelegram.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.exception.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.domain.service.DiscordToTelegramMessageService;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final DiscordToTelegramMessageService discordToTelegramMessageService;

    public DiscordMessageRouteService(DiscordToTelegramMessageService discordToTelegramMessageService) {
        this.discordToTelegramMessageService = discordToTelegramMessageService;
    }

    public void send(JsonNode data){
        try {
            discordToTelegramMessageService.sendMessageToTelegramChannel(data);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }
}
