package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Contract for handlers that process Telegram updates and synced message contexts.
 * Implementations declare the message type they support.
 */
public interface MessageHandler {
    /**
     * Handles an incoming Telegram update.
     * @param update the Telegram update to handle
     */
    void handle(Update update);
    /**
     * Handles a synced message coming from Discord.
     * @param messageContext the synced message context
     */
    void handle(MessageContext messageContext);
    /**
     * Returns the message type this handler supports.
     * @return the supported message type name
     */
    String getType();
}
