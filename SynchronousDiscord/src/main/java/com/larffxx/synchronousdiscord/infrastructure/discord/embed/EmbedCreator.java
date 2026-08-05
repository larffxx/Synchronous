package com.larffxx.synchronousdiscord.infrastructure.discord.embed;

import com.larffxx.synchronousdiscord.domain.context.EmbedContext;

public interface EmbedCreator<T> {
    EmbedContext createEmbedContext(T t);
}
