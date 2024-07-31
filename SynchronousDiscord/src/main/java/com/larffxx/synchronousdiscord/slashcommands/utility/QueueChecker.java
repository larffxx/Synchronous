package com.larffxx.synchronousdiscord.slashcommands.utility;

import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QueueChecker {

    public void queueCheck(List<AudioTrack> queue, EmbedBuilder embedBuilder) {
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
        embedBuilder.setDescription(CommandInfMessages.QUEUE_UNSUCCESSFUL_MESSAGE);
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
