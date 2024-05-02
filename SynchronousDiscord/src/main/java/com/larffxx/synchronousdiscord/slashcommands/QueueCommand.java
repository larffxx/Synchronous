package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class QueueCommand extends Command {
    private final CommandListener commandListener;
    public final ResultHandler resultHandler;

    public QueueCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, CommandListener commandListener, ResultHandler resultHandler) {
        super(kafkaTemplate, eventReceiver);
        this.commandListener = commandListener;
        this.resultHandler = resultHandler;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        putTrackInQueue(musicManager);
        event.reply("Queue of tracks").queue();
    }


    @Override
    public void execute(String commandName) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(getEventReceiver().getTextChannel().getGuild());
        putTrackInQueue(musicManager);
    }


    @Override
    public String getCommand() {
        return "queue";
    }

    private void putTrackInQueue(GuildMusicManager musicManager) {
        List<AudioTrack> queue = new ArrayList<>(musicManager.getScheduler().getQueue());
        EmbedBuilder eb = new EmbedBuilder().setTitle("Current queue");
        if (queue.isEmpty()) {
            eb.setDescription("Queue is empty");
        }
        for (int i = 0; i < queue.size(); i++) {
            eb.addField(i + 1 + ":", queue.get(i).getInfo().title, false)
                    .addField(i + 2 + ":", queue.get(i).getInfo().uri, false);
        }
        commandListener.getEmbedSender().send(eb);
    }
}
