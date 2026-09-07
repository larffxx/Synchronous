package com.larffxx.synchronousdiscord.infrastructure.sender;

/**
 * Sender contract.
 * @param <T> the T type parameter.
 */
public interface Sender <T> {
    /**
     * Sends data.
     * @param data the data.
     */
    void send(T data);
    /**
     * Returns sender.
     * @return the resulting string.
     */
    String getSender();
}
