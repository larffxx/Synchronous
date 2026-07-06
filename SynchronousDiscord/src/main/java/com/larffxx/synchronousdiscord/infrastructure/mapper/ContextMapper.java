package com.larffxx.synchronousdiscord.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;

public interface ContextMapper <T> {
    T toContext(JsonNode node);
}
