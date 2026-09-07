package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

/**
 * Contract for senders that deliver synced messages to Telegram.
 * Implementations cover text and media delivery.
 */
public interface Sender {
    /**
     * Sends the given synced message to Telegram.
     * @param messageContext the synced message context to deliver
     */
    void send(MessageContext messageContext);
}
