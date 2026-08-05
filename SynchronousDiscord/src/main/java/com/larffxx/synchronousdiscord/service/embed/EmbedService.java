package com.larffxx.synchronousdiscord.service.embed;

import com.larffxx.synchronousdiscord.domain.context.EmbedContext;
import org.springframework.stereotype.Service;

@Service
public interface EmbedService <T> {
    EmbedContext getEmbedContext(T t);
    void sendEmbed(EmbedContext context);
}
