package com.larffxx.synchronousdiscord.infrastructure.mapper;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Context Mapper contract.
 * @param <T> the T type parameter.
 */
public interface ContextMapper <T> {
    /**
     * Converts JSON node to context object.
     * @param node the node.
     * @return the resulting t.
     */
    T toContext(JsonNode node);
}
