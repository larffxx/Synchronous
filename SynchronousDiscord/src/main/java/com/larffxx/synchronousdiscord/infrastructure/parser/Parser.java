package com.larffxx.synchronousdiscord.infrastructure.parser;

/**
 * Parser contract.
 * @param <T> the T type parameter.
 * @param <L> the L type parameter.
 */
public interface Parser <T, L> {
    /**
     * Parses t into l.
     * @param t the item.
     * @return the resulting l.
     */
    L parse(T t);
}
