package com.larffxx.synchronousdiscord.util.embed.creator;

import net.dv8tion.jda.api.EmbedBuilder;

public interface EmbedCreator<T> {
    EmbedBuilder buildEmbed(T t);
}
