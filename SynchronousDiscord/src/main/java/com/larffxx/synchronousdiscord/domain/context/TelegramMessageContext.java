package com.larffxx.synchronousdiscord.domain.context;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Telegram Message Context record.
 * @param guild the guild.
 * @param file the file.
 * @param message the message.
 * @param username the username.
 * @param telegramChatID the telegram chat id.
 * @param messageType the message type.
 * @param textChannel the text channel.
 */
public record TelegramMessageContext(
        Guild guild,
        String file,
        String message,
        String username,
        String telegramChatID,
        MessageType messageType,
        TextChannel textChannel
) {}
