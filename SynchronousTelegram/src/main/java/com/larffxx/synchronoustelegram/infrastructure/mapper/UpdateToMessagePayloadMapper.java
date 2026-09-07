package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

/**
 * Maps a Telegram update to a message payload.
 * Creates photo payloads when a photo file is supplied and text payloads otherwise.
 */
public class UpdateToMessagePayloadMapper implements PayloadMapper<MessagePayload> {

    /**
     * Converts the given update into a text message payload.
     * @param update the Telegram update holding the message
     * @param guildId the Discord guild linked to the chat
     * @return the mapped text message payload
     */
    @Override
    public MessagePayload mapToPayload(Update update, String guildId) {
        return mapToPayload(update, guildId, null);
    }

    /**
     * Converts the given update into a message payload, attaching the photo when provided.
     * @param update the Telegram update holding the message
     * @param guildId the Discord guild linked to the chat
     * @param photoFile optional converted photo to attach, may be null
     * @return the mapped message payload
     */
    public MessagePayload mapToPayload(Update update, String guildId, File photoFile) {
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        String message = update.getMessage().getText();

        if (photoFile != null) {
            return new MessagePayload(chatId, guildId, username, message, photoFile,
                    String.valueOf(MessageType.PHOTO_MESSAGE));
        }

        return new MessagePayload(chatId, guildId, username, message,
                String.valueOf(MessageType.TEXT_MESSAGE));
    }
}
