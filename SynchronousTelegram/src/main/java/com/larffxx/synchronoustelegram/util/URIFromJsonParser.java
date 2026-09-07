package com.larffxx.synchronoustelegram.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.MapperConstant;

import java.util.List;
import java.util.stream.StreamSupport;

/**
 * Extracts file URI strings from JSON payloads.
 */
public class URIFromJsonParser {

    /**
     * Collects file URI strings stored under the files key of the payload.
     *
     * @param data JSON payload containing file references
     * @return cleaned list of URI strings
     */
    public List<String> uriParse(JsonNode data) {
        List<JsonNode> rawUris = data.findValues(MapperConstant.FILES).stream().toList();

        return rawUris.stream()
                .flatMap(jsonnode-> StreamSupport.stream(jsonnode.spliterator(), false))
                .map(JsonNode::asText)
                .map(string -> string.replace("[", "").replace("]","").replace("\"", "").replace(" ",""))
                .toList();
    }
}
