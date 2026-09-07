package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.MapperConstant;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.util.PackageFilesLoader;
import com.larffxx.synchronoustelegram.util.URIFromJsonParser;

import java.io.File;
import java.util.List;

/**
 * Maps a Kafka JSON node to a message context.
 * Reads chat, guild, sender, text, and type fields and loads attached files when present.
 */
public class JsonNodeToMessageContextMapper implements ContextMapper<MessageContext> {

    /**
     * Converts the given JSON node into a message context, loading attached files when present.
     * @param jsonNode the JSON node to convert
     * @return the mapped message context
     */
    @Override
    public MessageContext mapToContext(JsonNode jsonNode) {
        Long guildID = jsonNode.get(MapperConstant.GUILD_ID).asLong();
        Long telegramChatId = jsonNode.get(MapperConstant.TELEGRAM_CHAT_ID).asLong();
        String discordUserName = jsonNode.get(MapperConstant.DISCORD_USER_NAME).asText();
        String message = jsonNode.get(MapperConstant.MESSAGE).asText();
        String messageType = jsonNode.get(MapperConstant.MESSAGE_TYPE).asText();

        PackageFilesLoader packageFilesLoader = new PackageFilesLoader();
        URIFromJsonParser uriFromJsonParser = new URIFromJsonParser();
        List<File> files = packageFilesLoader.getFilesFromURIs(uriFromJsonParser.uriParse(jsonNode));

        if(files.isEmpty()){
            return new MessageContext(telegramChatId, guildID,discordUserName,message, MessageType.valueOf(messageType));
        }
        return new MessageContext(telegramChatId, guildID, discordUserName, message,  MessageType.valueOf(messageType), files);
    }
}
