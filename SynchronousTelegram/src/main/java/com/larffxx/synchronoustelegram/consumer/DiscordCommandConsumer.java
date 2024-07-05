package com.larffxx.synchronoustelegram.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class DiscordCommandConsumer {
    private final CommandPreProcessor preProcessor;
    private final UpdateReceiver updateReceiver;
    private final ServersConnectDAO serversConnectDAO;
    private final String GUILD_ID_JSON_NODE = "guildId";
    private final String COMMAND_JSON_NODE = "command";
    private final Character COMMAND_PREFIX = '/';

    public DiscordCommandConsumer(UpdateReceiver updateReceiver, ServersConnectDAO serversConnectDAO, CommandPreProcessor preProcessor) {
        this.updateReceiver = updateReceiver;
        this.serversConnectDAO = serversConnectDAO;
        this.preProcessor = preProcessor;
    }

    @KafkaListener(topics = {"${dCTopic}"}, groupId = "${groupId}")
    public void listener(@Payload String command){
        try {
            JsonNode data = new ObjectMapper().readTree(command);
            updateReceiver.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(data.findValue(GUILD_ID_JSON_NODE).asText()).getTelegramChannel());
            StringBuilder builder = new StringBuilder(data.findValue(COMMAND_JSON_NODE).asText());
            builder.insert(0, COMMAND_PREFIX);
            Command com = preProcessor.getCommand(String.valueOf(builder));
            com.execute(updateReceiver);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
