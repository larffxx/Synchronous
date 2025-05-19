package com.larffxx.synchronoustelegram.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.parser.DiscordMessagePayloadParser;
import com.larffxx.synchronoustelegram.parser.URIFromJsonParser;
import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.sender.PhotoMessageSender;
import com.larffxx.synchronoustelegram.sender.TextMessageSender;
import com.larffxx.synchronoustelegram.sender.utility.PackageFilesLoader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Component
public class DiscordToTelegramMessageService {
    private final DiscordMessagePayloadParser discordMessagePayloadParser;
    private final PhotoMessageSender photoMessageSender;
    private final TextMessageSender textMessageSender;

    public DiscordToTelegramMessageService(TextMessageSender textMessageSender, PhotoMessageSender photoMessageSender, URIFromJsonParser uriFromJsonParser, PackageFilesLoader packageFilesLoader, DiscordMessagePayloadParser discordMessagePayloadParser) {
        this.textMessageSender = textMessageSender;
        this.photoMessageSender = photoMessageSender;
        this.discordMessagePayloadParser = discordMessagePayloadParser;
    }

    public void sendMessageToTelegramChannel(JsonNode data) throws TelegramApiException {
        DiscordPayload payload = discordMessagePayloadParser.parseDiscordPayload(data);

        if (payload.getFiles() == null) {
            textMessageSender.send(payload);
        } else {
            photoMessageSender.sendPhoto(payload);
        }
    }
}
