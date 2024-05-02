package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SkipTrackCommand extends Command{
    private final ResultHandler resultHandler;


    public SkipTrackCommand(KafkaTemplate kafkaTemplate, EventReceiver eventReceiver, ResultHandler resultHandler) {
        super(kafkaTemplate, eventReceiver);
        this.resultHandler = resultHandler;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();
        event.reply("Skipped").queue();
    }

    @Override
    public void execute(String commandName) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(getEventReceiver().getTextChannel().getGuild());
        musicManager.getScheduler().nextTrack();
    }

    @Override
    public String getCommand() {
        return "skip";
    }
}
