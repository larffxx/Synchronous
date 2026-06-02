package com.larffxx.synchronoustelegram.infrastructure.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.util.PackageFilesLoader;
import com.larffxx.synchronoustelegram.util.URIFromJsonParser;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
public class DiscordMessagePayloadParser {
    private final URIFromJsonParser uriFromJsonParser;
    private final PackageFilesLoader packageFilesLoader;

    public DiscordMessagePayloadParser(URIFromJsonParser uriFromJsonParser, PackageFilesLoader packageFilesLoader) {
        this.uriFromJsonParser = uriFromJsonParser;
        this.packageFilesLoader = packageFilesLoader;
    }

    public DiscordPayload parseDiscordMessage(JsonNode jsonPayload) {
        Long guildID = jsonPayload.get("guildId").asLong();
        String author = jsonPayload.get("authorName").asText();
        String message = jsonPayload.get("message").asText();
        String messageType = jsonPayload.get("messageType").asText();

        List<File> files = packageFilesLoader.getFilesFromURIs(uriFromJsonParser.uriParse(jsonPayload));

        if(files.isEmpty()){
            return new DiscordPayload(guildID,author,message,files, MessageType.valueOf(messageType));
        }

        return new DiscordPayload(guildID, author, message, files);
    }
}
