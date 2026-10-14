package com.larffxx.synchronoustelegram.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.time.Instant;
import java.util.List;

/**
 * Kafka payload that carries a Telegram message to Discord.
 * Holds the chat and guild identifiers, the sender name, the text, optional files, and the message type.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload{
    /**
     * Identifier of the Telegram chat where the message was sent.
     */
    private Long chatId;
    /**
     * Identifier of the linked Discord guild.
     */
    private String guildId;
    /**
     * Name of the Telegram user who sent the message.
     */
    private String telegramUserName;
    /**
     * Text content of the message.
     */
    private String message;
    /**
     * Single attached photo file, null for text messages.
     */
    private File file;
    /**
     * Attached files received with the message.
     */
    private List<File> files;
    /**
     * Type of the message, either text or photo.
     */
    private String messageType;
    /**
     * Stable Telegram message identifier.
     */
    private String messageId;
    /**
     * Human-readable Telegram chat title or username.
     */
    private String chatName;
    /**
     * Timestamp assigned by Telegram when the message was created.
     */
    private Instant sentAt;

    /**
     * Creates a text message payload without files.
     * @param chatId the Telegram chat identifier
     * @param guildId the linked Discord guild identifier
     * @param telegramUserName the sender name
     * @param message the message text
     * @param type the message type
     */
    public MessagePayload(Long chatId, String guildId, String telegramUserName, String message, String type) {
        this.guildId = guildId;
        this.telegramUserName = telegramUserName;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }

    /**
     * Creates a message payload with a single attached photo.
     * @param chatId the Telegram chat identifier
     * @param guildId the linked Discord guild identifier
     * @param telegramUserName the sender name
     * @param message the message text
     * @param photoFile the attached photo file
     * @param messageType the message type
     */
    public MessagePayload(Long chatId, String guildId, String telegramUserName, String message, File photoFile, String messageType) {
        this.chatId = chatId;
        this.guildId = guildId;
        this.telegramUserName = telegramUserName;
        this.message = message;
        this.file = photoFile;
        this.messageType = messageType;
    }

    /**
     * Creates a message payload with source metadata for downstream consumers.
     * The additional fields are additive and preserve the existing bridge payload.
     * @param chatId the Telegram chat identifier
     * @param guildId the linked Discord guild identifier
     * @param telegramUserName the Telegram user name
     * @param message the message text
     * @param photoFile the optional photo file
     * @param messageType the message type
     * @param messageId the Telegram message id
     * @param chatName the Telegram chat name
     * @param sentAt the source message timestamp
     */
    public MessagePayload(Long chatId, String guildId, String telegramUserName, String message,
                          File photoFile, String messageType, String messageId,
                          String chatName, Instant sentAt) {
        this.chatId = chatId;
        this.guildId = guildId;
        this.telegramUserName = telegramUserName;
        this.message = message;
        this.file = photoFile;
        this.messageType = messageType;
        this.messageId = messageId;
        this.chatName = chatName;
        this.sentAt = sentAt;
    }


    /**
     * Returns a short human readable summary of the payload.
     * @return payload summary with sender name and message
     */
    @Override
    public String toString() {
        return "Name: " + this.telegramUserName + " Message: " + this.message;
    }
}

