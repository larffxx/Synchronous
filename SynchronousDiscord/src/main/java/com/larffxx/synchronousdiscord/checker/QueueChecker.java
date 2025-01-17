package com.larffxx.synchronousdiscord.checker;

import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueueChecker{

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
            lessThanTenCheck(queue, embedBuilder);
        }
    }

    private void lessThanTenCheck(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        if (queue.size() < 10) {
            queueLessThanTen(queue, embedBuilder);
        } else {
            queueMoreThanTen(queue, embedBuilder);
        }
    }

    private void emptyQueue(EmbedBuilder embedBuilder) {
        embedBuilder.setDescription(CommandConstants.QUEUE_UNSUCCESSFUL_MESSAGE);
    }

    private void queueMoreThanTen(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        for (int i = 0; i < 10; i++) {
            embedBuilder.addField(i + ":", queue.get(i).getInfo().title, false);
        }
    }

    private void queueLessThanTen(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
        for (int i = 0; i < queue.size(); i++) {
            embedBuilder.addField(i + ":", queue.get(i).getInfo().title, false);
        }
    }
}
