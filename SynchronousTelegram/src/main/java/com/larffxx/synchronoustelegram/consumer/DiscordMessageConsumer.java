package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.commands.SendTextMessage;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Getter
@Setter
public class DiscordMessageConsumer {

    private final UpdateReceiver updateReceiver;
    private final ServersConnectDAO serversConnectDAO;
    private final GuildProfileDAO guildProfileDAO;
    private final SendTextMessage sendTextMessage;

    public DiscordMessageConsumer(UpdateReceiver updateReceiver, ServersConnectDAO serversConnectDAO, GuildProfileDAO guildProfileDAO, SendTextMessage sendTextMessage) {
        this.updateReceiver = updateReceiver;
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
            updateReceiver.setChatId(serversConnectDAO.getChatByDiscordGuild(data.findValue("chatId").asText()).getTelegramChannel());
            if (matcher.find()) {
                String formattedMSG = data.findValue("message").asText().replace(matcher.group(),
                        "@" + guildProfileDAO.getByName(matcher.group().replace("@", "")).getUsersConnect().getTelegramName());
                sendTextMessage.execute(Long.valueOf(updateReceiver.getChatId()), data.findValue("name").asText() + ": " + formattedMSG);
            } else {
                sendTextMessage.execute(Long.valueOf(updateReceiver.getChatId()), data.findValue("name").asText() + ": " + data.findValue("message").asText());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
