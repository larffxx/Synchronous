package com.larffxx.synchronousdiscord.infrastructure.discord.embed;

import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.domain.context.EmbedContext;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueueEmbedCreator implements EmbedCreator<GuildMusicManager> {

    @Override
    public EmbedContext createEmbedContext(GuildMusicManager guildMusicManager) {
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getScheduler().getQueue());
        List<String> titles = new ArrayList<>(guildMusicManager.getScheduler().getQueue()).stream().map(t -> t.getInfo().title).toList();
        EmbedBuilder embedBuilder = new EmbedBuilder();

        createEmbedQueueList(queue, embedBuilder);

        return new EmbedContext(embedBuilder, titles);
    }

    private void createEmbedQueueList(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        if (queue.isEmpty()) {
            embedBuilder.setDescription(CommandConstants.QUEUE_UNSUCCESSFUL_MESSAGE);
            return;
        }

        int displayLimit = Math.min(queue.size(), 10);

        for (int i = 0; i < displayLimit; i++) {
            embedBuilder.addField(i + ":", queue.get(i).getInfo().title, false);
        }

        if (queue.size() > 10) {
            embedBuilder.addField("And " + (queue.size() - 10) + " more...", "", false);
        }
    }
}
