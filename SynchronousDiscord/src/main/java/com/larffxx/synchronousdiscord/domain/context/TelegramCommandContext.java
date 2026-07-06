package com.larffxx.synchronousdiscord.domain.context;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

public record TelegramCommandContext(
        String command,
        String telegramUsername,
        String telegramChatID,
        List<String> options,
        TextChannel textChannel,
        Guild guild
) {
}
