package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

/**
 * Contract for services that send one kind of Telegram message.
 */
public interface MessageService {
    /**
     * Sends a message built from the given context.
     *
     * @param messageContext classified message context to send
     */
    void send(MessageContext messageContext);

    /**
     * Returns the message type handled by this service.
     *
     * @return message type identifier
     */
    String getType();
}
