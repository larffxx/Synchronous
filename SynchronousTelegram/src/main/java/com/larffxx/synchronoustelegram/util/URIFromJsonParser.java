package com.larffxx.synchronoustelegram.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.StreamSupport;

public class URIFromJsonParser {

    public List<String> uriParse(JsonNode data) {
        List<JsonNode> rawUris = data.findValues("files").stream().toList();

        return rawUris.stream()
                .flatMap(jsonnode-> StreamSupport.stream(jsonnode.spliterator(), false))
                .map(JsonNode::asText)
                .map(string -> string.replace("[", "").replace("]","").replace("\"", "").replace(" ",""))
                .toList();
    }
}
