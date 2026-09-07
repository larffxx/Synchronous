package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Contract for mappers that convert raw Kafka JSON nodes into typed contexts.
 * Implementations cover command and message contexts.
 */
public interface ContextMapper <T> {
    /**
     * Maps a JSON node to a typed context.
     * @param node the JSON node to map
     * @return the mapped context
     */
    T mapToContext(JsonNode node);
}
