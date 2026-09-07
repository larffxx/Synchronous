package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.MapperConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Maps a Kafka JSON node to a command context.
 * Reads chat id, command name, executor, options, and response fields from the node.
 */
public class JsonNodeToCommandContextMapper implements ContextMapper<CommandContext> {

    /**
     * Converts the given JSON node into a command context.
     * @param jsonNode the JSON node to convert
     * @return the mapped command context
     */
    @Override
    public CommandContext mapToContext(JsonNode jsonNode) {
        Long chatId = jsonNode.get(MapperConstant.TELEGRAM_CHAT_ID).asLong();
        String commandName = jsonNode.get(MapperConstant.COMMAND_NAME).asText();
        String executedBy = jsonNode.get(MapperConstant.DISCORD_USER_NAME).asText();
        List<String> options = StreamSupport.stream(jsonNode.get(MapperConstant.OPTIONS).spliterator(), false)
                .map(JsonNode::asText)
                .toList();
        List<String> response = StreamSupport.stream(jsonNode.get(MapperConstant.RESPONSE).spliterator(),false)
                .map(JsonNode::asText)
                .toList();


        return new CommandContext(chatId, executedBy, commandName, options,response);
    }
}
