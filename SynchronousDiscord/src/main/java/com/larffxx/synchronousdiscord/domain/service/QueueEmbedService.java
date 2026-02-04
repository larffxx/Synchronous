package com.larffxx.synchronousdiscord.domain.service;

import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.util.embed.creator.EmbedCreator;
import com.larffxx.synchronousdiscord.util.embed.creator.QueueEmbedCreator;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.infrastructure.sender.embed.EmbedSender;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueueEmbedService {
    private final Sender<EmbedBuilder> embedSender;
    private final EmbedCreator<GuildMusicManager> embedCreator;

    public QueueEmbedService(EmbedSender embedSender, EmbedCreator<GuildMusicManager> embedCreator, QueueEmbedCreator queueEmbedCreator) {
        this.embedSender = embedSender;
        this.embedCreator = embedCreator;
    }

    public void sendQueueEmbed(GuildMusicManager guildMusicManager) {
        EmbedBuilder builder = embedCreator.buildEmbed(guildMusicManager);

        embedSender.send(builder);
    }
}
