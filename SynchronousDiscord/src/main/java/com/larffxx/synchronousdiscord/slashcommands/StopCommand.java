package com.larffxx.synchronousdiscord.slashcommands;


import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StopCommand extends Command{
    private final ResultHandler resultHandler;

    public StopCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, ResultHandler resultHandler) {
        super(kafkaTemplate, eventReceiver);
        this.resultHandler = resultHandler;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(event.getGuild());
        guildMusicManager.getScheduler().stopTrack();
        event.reply("Stopped").queue();
        AudioManager manager = event.getGuild().getAudioManager();
        manager.closeAudioConnection();
    }

    @Override
    public void execute(String commandName) {
        GuildMusicManager guildMusicManager = resultHandler.getMusicManager(getEventReceiver().getTextChannel().getGuild());
        guildMusicManager.getScheduler().stopTrack();
    }

    @Override
    public String getCommand() {
        return "stop";
    }
}
