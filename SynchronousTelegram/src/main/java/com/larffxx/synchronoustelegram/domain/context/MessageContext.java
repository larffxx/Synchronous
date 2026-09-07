package com.larffxx.synchronoustelegram.domain.context;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

/**
 * Mutable context describing a synced message.
 * Carries the Telegram chat, the Discord guild, the sender, the text, the message type, and optional files.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageContext {
        /**
         * Identifier of the Telegram chat for the message.
         */
        private Long telegramChatId;
        /**
         * Identifier of the linked Discord guild.
         */
        private Long guildId;
        /**
         * Name of the user who sent the message.
         */
        private String user;
        /**
         * Text content of the message.
         */
        private String message;
        /**
         * Type of the message.
         */
        private MessageType messageType;
        /**
         * Files attached to the message.
         */
        private List<File> fileList;

        /**
         * Creates a message context without attached files.
         * @param telegramChatId the Telegram chat identifier
         * @param guildId the linked Discord guild identifier
         * @param user the sender name
         * @param message the message text
         * @param messageType the message type
         */
        public MessageContext(Long telegramChatId, Long guildId, String user, String message, MessageType messageType) {
            this.telegramChatId = telegramChatId;
            this.guildId = guildId;
            this.user = user;
            this.message = message;
            this.messageType = messageType;
        }
}
