package com.larffxx.synchronousdiscord.infrastructure;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.context.DiscordAudioContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import org.springframework.stereotype.Component;


@Component
public class DiscordContextResolver {
    private final ResultHandler resultHandler;

    public DiscordContextResolver(ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public DiscordAudioContext resolveContext(TelegramCommandContext telegramCommandContext) {
        GuildMusicManager manager = resultHandler.getMusicManager(telegramCommandContext.guild());

        return new DiscordAudioContext(telegramCommandContext.guild(), telegramCommandContext.textChannel(), manager);
    }
}
