package com.larffxx.synchronoustelegram.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class URIFromJsonParser {

    public List<String> uriParse(JsonNode data) {
        List<JsonNode> rawUris = data.findValues("files").stream().toList();
        List<String> formatedJsonFiles = rawUris.stream()
                .flatMap(jsonnode-> StreamSupport.stream(jsonnode.spliterator(), false))
                .map(JsonNode::asText)
                .map(string -> string.replace("[", "").replace("]","").replace("\"", "").replace(" ",""))
                .toList();

        return formatedJsonFiles;
    }
}
