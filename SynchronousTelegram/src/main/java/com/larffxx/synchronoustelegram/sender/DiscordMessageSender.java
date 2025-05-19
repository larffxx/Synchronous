package com.larffxx.synchronoustelegram.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.parser.URIFromJsonParser;
import com.larffxx.synchronoustelegram.sender.utility.PackageFilesLoader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
@Component
public class DiscordMessageSender {
    private final ServersConnectDAO serversConnectDAO;
    private final UpdateHolder updateHolder;
    private final GuildProfileDAO guildProfileDAO;
    private final PhotoMessageSender photoMessageSender;
    private final TextMessageSender textMessageSender;
    private final URIFromJsonParser uriFromJsonParser;
    private final PackageFilesLoader packageFilesLoader;
    private Matcher matcher;

    public DiscordMessageSender(UpdateHolder updateHolder, GuildProfileDAO guildProfileDAO, TextMessageSender textMessageSender, ServersConnectDAO serversConnectDAO, PhotoMessageSender photoMessageSender, URIFromJsonParser uriFromJsonParser, PackageFilesLoader packageFilesLoader) {
        this.updateHolder = updateHolder;
        this.guildProfileDAO = guildProfileDAO;
        this.textMessageSender = textMessageSender;
        this.serversConnectDAO = serversConnectDAO;
        this.photoMessageSender = photoMessageSender;
        this.uriFromJsonParser = uriFromJsonParser;
        this.packageFilesLoader = packageFilesLoader;
    }

    public void sendTextMessageToTelegramChannel(JsonNode data) throws TelegramApiException {
        String guildId = data.findValue("guildId").asText();
        String message = data.findValue("message").asText();
        updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(guildId).getTelegramChannel());
        Long chatId = Long.valueOf(updateHolder.getChatId());
        List<File> files = packageFilesLoader.getFilesFromURIs(uriFromJsonParser.parse(data));

        setMatcher(message);
        if(data.findValue("files").asText() == null) {

            if (matcher.find()) {
                textMessageSender.send(chatId, formatMsg(message));
            } else {
                textMessageSender.send(chatId, message);
            }
        }else {
            if(message != null){
                photoMessageSender.send(chatId, message, files);
            }
            photoMessageSender.send(chatId, files);
        }
    }

    private void setMatcher(String msg){
        String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
        matcher = Pattern.compile(USERNAME_PATTER).matcher(msg);
    }

    private String formatMsg(String msg){
        String discordName = matcher.group().replace("@","");
        return msg.replace(matcher.group(),
                "@" + guildProfileDAO.getByName(discordName).getUsersConnect().getTelegramName());
    }

}
