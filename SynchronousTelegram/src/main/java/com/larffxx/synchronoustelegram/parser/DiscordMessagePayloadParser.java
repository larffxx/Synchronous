package com.larffxx.synchronoustelegram.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.sender.utility.PackageFilesLoader;
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

        List<File> files = packageFilesLoader.getFilesFromURIs(uriFromJsonParser.uriParse(jsonPayload));

        return new DiscordPayload(guildID, author, message, files);
    }
}
