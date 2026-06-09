package com.larffxx.synchronoustelegram.infrastructure.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

//TODO: Delete
@Component
public class DiscordCommandPayloadParser {
    private final ServersConnectRepository serversConnectRepository;

    public DiscordCommandPayloadParser(ServersConnectRepository serversConnectRepository) {
        this.serversConnectRepository = serversConnectRepository;
    }

    public DiscordPayload parseDiscordCommand(JsonNode data) {
        Long guildID = data.get("guildId").asLong();
        String author = data.get("authorName").asText();
        String commandName = data.get("command").asText();
        List<String> options = StreamSupport.stream(data.get("options").spliterator(), false)
                .map(JsonNode::asText)
                .toList();

        String telegramChatId = serversConnectRepository.findByDiscordGuild(String.valueOf(guildID)).getTelegramChannel();
        CommandPayload commandPayload = new CommandPayload(telegramChatId, author, commandName, options);

        return new DiscordPayload(guildID, author, commandPayload);
    }
}
