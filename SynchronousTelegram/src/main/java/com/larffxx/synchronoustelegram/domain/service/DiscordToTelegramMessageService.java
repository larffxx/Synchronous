package com.larffxx.synchronoustelegram.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordMessagePayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.sender.PhotoMessageSender;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordToTelegramMessageService {
    private final DiscordMessagePayloadParser discordMessagePayloadParser;
    private final PhotoMessageSender photoMessageSender;
    private final TextMessageSender textMessageSender;

    public DiscordToTelegramMessageService(TextMessageSender textMessageSender, PhotoMessageSender photoMessageSender, DiscordMessagePayloadParser discordMessagePayloadParser) {
        this.textMessageSender = textMessageSender;
        this.photoMessageSender = photoMessageSender;
        this.discordMessagePayloadParser = discordMessagePayloadParser;
    }

    public void sendMessageToTelegramChannel(JsonNode data) throws TelegramApiException {
        DiscordPayload payload = discordMessagePayloadParser.parseDiscordMessage(data);

        if (!payload.getFiles().isEmpty()) {
            photoMessageSender.sendPhoto(payload);
        } else {
            textMessageSender.send(payload);
        }
    }
}
