package com.larffxx.synchronoustelegram.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.exception.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.infexc.InfExcMessage;
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
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }
}
