package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.MapperConstant;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.util.PackageFilesLoader;
import com.larffxx.synchronoustelegram.util.URIFromJsonParser;

import java.io.File;
import java.util.List;

public class JsonNodeToMessageContextMapper implements ContextMapper<MessageContext> {

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
