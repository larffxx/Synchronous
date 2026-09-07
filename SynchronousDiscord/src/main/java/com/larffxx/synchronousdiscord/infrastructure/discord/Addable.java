package com.larffxx.synchronousdiscord.infrastructure.discord;

/**
 * Addable contract.
 * @param <T> the T type parameter.
 */
public interface Addable <T> {
    /**
     * Adds item.
     * @param t the item.
     */
    void add(T t);
}
