package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.record.CommandContext;

import java.util.ArrayList;
import java.util.List;

public class JsonNodeToCommandContextMapper {

    //TODO: mapping
    public CommandContext toCommandContext(JsonNode jsonNode) {
        Long chatId = 0L;
        String commandName = "";
        String commandAuthor = "";
        List<String> options = new ArrayList<>();


        return new CommandContext(chatId, commandAuthor, commandName, options);
    }
}
