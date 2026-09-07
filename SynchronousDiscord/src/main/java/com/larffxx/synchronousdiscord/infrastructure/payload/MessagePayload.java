package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 * Message Payload class.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessagePayload {
    /**
     * The guild id.
     */
    private Long guildId;
    /**
     * The telegram chat id.
     */
    private Long telegramChatId;
    /**
     * The discord user name.
     */
    private String discordUserName;
    /**
     * The message.
     */
    private String message;
    /**
     * The files.
     */
    private List<File> files;
    /**
     * The message type.
     */
    private MessageType messageType;

    /**
     * Creates a new MessagePayload.
     * @param guildId the guild id.
     * @param telegramChatId the telegram chat id.
     * @param discordUserName the discord user name.
     * @param message the message.
     * @param files the files.
     */
    public MessagePayload(Long guildId, Long telegramChatId, String discordUserName, String message, List<File> files) {
        this.guildId = guildId;
        this.telegramChatId = telegramChatId;
        this.discordUserName = discordUserName;
        this.message = message;
        this.files = files;
        this.messageType = MessageType.PHOTO_MESSAGE;
    }
}
