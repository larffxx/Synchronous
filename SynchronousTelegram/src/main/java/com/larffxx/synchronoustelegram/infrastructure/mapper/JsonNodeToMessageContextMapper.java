package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.util.PackageFilesLoader;
import com.larffxx.synchronoustelegram.util.URIFromJsonParser;

import java.io.File;
import java.util.List;

public class JsonNodeToMessageContextMapper {

    public MessageContext toMessageContext(JsonNode jsonNode) {
        Long guildID = jsonNode.get("guildId").asLong();
        Long telegramChatId = jsonNode.get("telegramChatId").asLong();
        String author = jsonNode.get("authorName").asText();
        String message = jsonNode.get("message").asText();
        String messageType = jsonNode.get("messageType").asText();

        PackageFilesLoader packageFilesLoader = new PackageFilesLoader();
        URIFromJsonParser uriFromJsonParser = new URIFromJsonParser();
        List<File> files = packageFilesLoader.getFilesFromURIs(uriFromJsonParser.uriParse(jsonNode));

        if(files.isEmpty()){
            return new MessageContext(telegramChatId, guildID,author,message, MessageType.valueOf(messageType));
        }
        return new MessageContext(telegramChatId, guildID, author, message,  MessageType.valueOf(messageType), files);
    }
}
