package com.larffxx.synchronousdiscord.service.embed;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.domain.context.EmbedContext;
import com.larffxx.synchronousdiscord.infrastructure.discord.embed.EmbedCreator;
import com.larffxx.synchronousdiscord.infrastructure.discord.embed.QueueEmbedCreator;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.infrastructure.sender.embed.EmbedSender;
import org.springframework.stereotype.Component;

@Component
public class QueueEmbedService implements EmbedService <GuildMusicManager> {
    private final Sender<EmbedContext> embedSender;
    private final EmbedCreator<GuildMusicManager> embedCreator;

    public QueueEmbedService(EmbedSender embedSender, EmbedCreator<GuildMusicManager> embedCreator, QueueEmbedCreator queueEmbedCreator) {
        this.embedSender = embedSender;
        this.embedCreator = embedCreator;
    }

    public EmbedContext getEmbedContext(GuildMusicManager guildMusicManager) {
        return embedCreator.createEmbedContext(guildMusicManager);
    }

    public void sendEmbed(EmbedContext embedContext) {
        embedSender.send(embedContext);
    }

}
