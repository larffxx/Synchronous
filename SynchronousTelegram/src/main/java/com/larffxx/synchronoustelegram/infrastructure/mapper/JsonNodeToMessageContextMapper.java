package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.domain.record.MessageContext;

public class JsonNodeToMessageContextMapper {

    //TODO: mapping
    public MessageContext toMessageContext(JsonNode jsonNode) {

        return new MessageContext();
    }
}
