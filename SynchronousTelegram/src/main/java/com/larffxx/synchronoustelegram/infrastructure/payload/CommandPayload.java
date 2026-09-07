package com.larffxx.synchronoustelegram.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Kafka payload that carries a Telegram command to Discord.
 * Holds the chat and guild identifiers, the sender name, the command, and its options.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommandPayload{
    /**
     * Identifier of the Telegram chat where the command was issued.
     */
    private String chatId;
    /**
     * Identifier of the linked Discord guild.
     */
    private String guildId;
    /**
     * Name of the Telegram user who issued the command.
     */
    private String telegramUserName;
    /**
     * Name of the issued command.
     */
    private String command;
    /**
     * Options supplied with the command.
     */
    private List<String> options;
}
