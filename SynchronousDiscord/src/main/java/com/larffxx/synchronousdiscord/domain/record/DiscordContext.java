package com.larffxx.synchronousdiscord.domain.record;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public record DiscordContext(
        Guild guild,
        TextChannel textChannel,
        GuildMusicManager guildMusicManager
) {}
