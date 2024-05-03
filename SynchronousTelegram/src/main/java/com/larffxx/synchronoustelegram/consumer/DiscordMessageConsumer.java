package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.commands.SendText;
import com.larffxx.synchronoustelegram.dao.GuildProfileDAO;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SendText sendText;

    public DiscordMessageConsumer(UpdateReceiver updateReceiver, ServersConnectDAO serversConnectDAO, GuildProfileDAO guildProfileDAO, SendText sendText) {
        this.updateReceiver = updateReceiver;
        this.serversConnectDAO = serversConnectDAO;
        this.guildProfileDAO = guildProfileDAO;
        this.sendText = sendText;
    }

    @KafkaListener(
            topics = {"${dTopic}"},
            groupId = "${groupId}"
    )
    public void listener(@Payload String user) {
        try {
            JsonNode data = (new ObjectMapper()).readTree(user);
            String USERNAME_PATTER = "@([a-zA-Z0-9\\._\\-]{3,})";
            Matcher matcher = Pattern.compile(USERNAME_PATTER).matcher(data.findValue("message").asText());
            updateReceiver.setChatId(serversConnectDAO.getChatByDiscordGuild(data.findValue("chatId").asText()).getTelegramChannel());
            if (matcher.find()) {
                String formattedMSG = data.findValue("message").asText().replace(matcher.group(),
                        "@" + guildProfileDAO.getByName(matcher.group().replace("@", "")).getUsersConnect().getTelegramName());
                sendText.execute(Long.valueOf(updateReceiver.getChatId()), data.findValue("name").asText() + ": " + formattedMSG);
            } else {
                sendText.execute(Long.valueOf(updateReceiver.getChatId()), data.findValue("name").asText() + ": " + data.findValue("message").asText());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
