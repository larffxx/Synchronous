package com.larffxx.synchronousdiscord.domain.context;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

/**
 * Telegram Command Context record.
 * @param command the command.
 * @param telegramUsername the telegram username.
 * @param telegramChatID the telegram chat id.
 * @param options the options.
 * @param textChannel the text channel.
 * @param guild the guild.
 */
public record TelegramCommandContext(
        String command,
        String telegramUsername,
        String telegramChatID,
        List<String> options,
        TextChannel textChannel,
        Guild guild
) {
}
