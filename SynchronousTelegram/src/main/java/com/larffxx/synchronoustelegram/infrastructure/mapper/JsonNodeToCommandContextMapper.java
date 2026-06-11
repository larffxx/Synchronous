package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;

import java.util.List;
import java.util.stream.StreamSupport;

public class JsonNodeToCommandContextMapper {

    public CommandContext toCommandContext(JsonNode jsonNode) {
        Long chatId = jsonNode.get("telegramChatId").asLong();
        String commandName = jsonNode.get("commandName").asText();
        String commandAuthor = jsonNode.get("authorName").asText();
        List<String> options = StreamSupport.stream(jsonNode.get("options").spliterator(), false)
                .map(JsonNode::asText)
                .toList();


        return new CommandContext(chatId, commandAuthor, commandName, options);
    }
}
