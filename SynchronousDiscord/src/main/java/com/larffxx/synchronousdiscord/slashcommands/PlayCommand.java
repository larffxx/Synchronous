package com.larffxx.synchronousdiscord.slashcommands;

import com.larffxx.synchronousdiscord.lavaplayer.PlayerManager;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class PlayCommand extends Command {
    private final PlayerManager playerManager;
    private String link;

    public PlayCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, PlayerManager playerManager) {
        super(kafkaTemplate, eventReceiver);
        this.playerManager = playerManager;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            link = event.getOption("type").getAsString();
            Guild guild = event.getGuild();

            VoiceChannel channel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            AudioManager manager = guild.getAudioManager();

            manager.openAudioConnection(channel);

            playerManager.loadAndPlay(event.getChannel().asTextChannel(), link);
            event.reply("Track was added").queue();
        }
    }

    @Override
    public void execute(String commandName) {
        String[] getLink = commandName.split(" ");
        link = getLink[1];
        Guild guild = getEventReceiver().getTextChannel().getGuild();

        VoiceChannel channel = guild.getVoiceChannelsByName("Clown Party", true).get(0);
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);

        playerManager.loadAndPlay(getEventReceiver().getTextChannel(), link);
    }


    @Override
    public String getCommand() {
        return "play";
    }
}
