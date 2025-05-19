package com.larffxx.synchronousdiscord.embed;

import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiscordQueueEmbed {

    public EmbedBuilder createEmbed(GuildMusicManager guildMusicManager) {
        List<AudioTrack> queue = new ArrayList<>(guildMusicManager.getScheduler().getQueue());
        EmbedBuilder embedBuilder = new EmbedBuilder();

        queueCheck(queue, embedBuilder);

        return embedBuilder;
    }

    private void queueCheck(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        if (queue.isEmpty()) {
            emptyQueue(embedBuilder);
        } else {
            createQueue(queue, embedBuilder);
        }
    }

    private void emptyQueue(EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(CommandConstants.QUEUE_UNSUCCESSFUL_MESSAGE);
    }

    private void createQueue(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        int displayLimit = Math.min(queue.size(), 10);

        for (int i = 0; i < displayLimit; i++) {
            embedBuilder.addField(i + ":", queue.get(i).getInfo().title, false);
        }

        if (queue.size() > 10) {
            embedBuilder.addField("And " + (queue.size() - 10) + " more...", "", false);
        }
    }
}
