package com.larffxx.synchronousdiscord.infrastructure;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.utility.DiscordEntityResolver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Component;

import static com.larffxx.synchronousdiscord.infrastructure.sender.utility.DiscordEntityResolver.resolveTextChannel;

@Component
public class DiscordContextResolver {
    private final ResultHandler resultHandler;
    private final DiscordEntityResolver discordEntityResolver;

    public DiscordContextResolver(ResultHandler resultHandler, DiscordEntityResolver discordEntityResolver) {
        this.resultHandler = resultHandler;
        this.discordEntityResolver = discordEntityResolver;
    }

    public DiscordContext resolveContext(String telegramChatID) {
        String guildID = discordEntityResolver.resolveGuildId(telegramChatID);
        Guild guild = discordEntityResolver.resolveGuild(guildID);
        TextChannel textChannel = resolveTextChannel(guild);
        GuildMusicManager manager = resultHandler.getMusicManager(guild);

        return new DiscordContext(guild, textChannel, manager);
    }
}
