package com.larffxx.synchronoustelegram.infrastructure.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.payload.utility.Command;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class DiscordCommandPayloadParser {

    public DiscordPayload parseDiscordCommand(JsonNode data) {
        Long guildID = data.get("guildId").asLong();
        String author = data.get("authorName").asText();
        String commandName = data.get("command").asText();
        List<String> options = StreamSupport.stream(data.get("options").spliterator(), false)
                .map(JsonNode::asText)
                .toList();

        Command command = new Command(createSlashCommand(commandName), options);

        return new DiscordPayload(guildID, author, command);
    }

    private String createSlashCommand(String command) {
        StringBuilder builder = new StringBuilder(command);

        Character COMMAND_PREFIX = '/';
        builder.insert(0, COMMAND_PREFIX);

        return String.valueOf(builder);
    }

}
