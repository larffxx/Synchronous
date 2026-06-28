package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContextMapper <T> {
    T mapToContext(JsonNode node);
}
