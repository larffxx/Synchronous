package com.larffxx.synchronousdiscord.infrastructure.discord.embed;

import net.dv8tion.jda.api.EmbedBuilder;

public interface EmbedCreator<T> {
    EmbedBuilder buildEmbed(T t);
}
