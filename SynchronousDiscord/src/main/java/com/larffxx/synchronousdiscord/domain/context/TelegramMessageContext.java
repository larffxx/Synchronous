package com.larffxx.synchronousdiscord.domain.context;

import com.larffxx.synchronousdiscord.infrastructure.payload.MessageType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public record TelegramMessageContext(
        Guild guild,
        String file,
        String message,
        String username,
        String telegramChatID,
        MessageType messageType,
        TextChannel textChannel
) {}
