package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.commands.SendTextMessage;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.exception.TelegramException;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
public class DiscordMessageConsumer {

    private final UpdateHolder updateHolder;
    private final ServersConnectDAO serversConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final SendTextMessage sendTextMessage;

    public DiscordMessageConsumer(UpdateHolder updateHolder, ServersConnectDAO serversConnectDAO, GuildProfileDAO guildProfileDAO, SendTextMessage sendTextMessage) {
        this.updateHolder = updateHolder;
        this.serversConnectDAO = serversConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.sendTextMessage = sendTextMessage;
    }

    @KafkaListener(topics = {"${dMTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String message) {
        try {
            JsonNode data = (new ObjectMapper()).readTree(message);
            String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
            Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue("message").asText());
            updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(data.findValue("guildId").asText()).getTelegramChannel());

            if (matcher.find()) {
                String formattedMSG = data.findValue("message").asText().replace(matcher.group(),
                        "@" + guildProfileDAO.getByName(matcher.group().replace("@", "")).getUsersConnect().getTelegramName());
                sendTextMessage.execute(Long.valueOf(updateHolder.getChatId()), data.findValue("name").asText() + ": " + formattedMSG);
            } else {
                sendTextMessage.execute(Long.valueOf(updateHolder.getChatId()), data.findValue("name").asText() + ": " + data.findValue("message").asText());
            }
        } catch (JsonProcessingException | TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
