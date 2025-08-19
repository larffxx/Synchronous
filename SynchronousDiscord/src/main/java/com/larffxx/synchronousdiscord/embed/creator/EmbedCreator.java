package com.larffxx.synchronousdiscord.embed.creator;

import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
public interface EmbedCreator<T> {
    EmbedBuilder buildEmbed(T t);
}
