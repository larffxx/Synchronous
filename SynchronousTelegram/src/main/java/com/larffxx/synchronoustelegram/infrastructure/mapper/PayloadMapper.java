package com.larffxx.synchronoustelegram.infrastructure.mapper;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Contract for mappers that convert Telegram updates into Kafka payloads.
 * Implementations cover command and message payloads.
 */
public interface PayloadMapper <T> {
    /**
     * Maps a Telegram update to a Kafka payload.
     * @param update the Telegram update to map
     * @param guildId the Discord guild linked to the chat
     * @return the mapped payload
     */
    T mapToPayload(Update update, String guildId);
}
