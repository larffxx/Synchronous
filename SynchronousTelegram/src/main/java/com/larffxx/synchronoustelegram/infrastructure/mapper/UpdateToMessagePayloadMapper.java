package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.time.Instant;

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
        var telegramMessage = update.getMessage();
        Long chatId = telegramMessage.getChatId();
        String username = telegramMessage.getFrom() == null
                ? "Unknown user"
                : telegramMessage.getFrom().getUserName();
        String message = telegramMessage.getText() != null
                ? telegramMessage.getText()
                : telegramMessage.getCaption();
        String chatName = telegramMessage.getChat().getTitle() != null
                ? telegramMessage.getChat().getTitle()
                : telegramMessage.getChat().getUserName();
        String messageId = String.valueOf(telegramMessage.getMessageId());
        Instant sentAt = telegramMessage.getDate() == null
                ? Instant.now()
                : Instant.ofEpochSecond(telegramMessage.getDate());

        if (photoFile != null) {
            return new MessagePayload(chatId, guildId, username, message, photoFile,
                    String.valueOf(MessageType.PHOTO_MESSAGE), messageId, chatName, sentAt);
        }

        return new MessagePayload(chatId, guildId, username, message,
                null, String.valueOf(MessageType.TEXT_MESSAGE), messageId, chatName, sentAt);
    }
}
