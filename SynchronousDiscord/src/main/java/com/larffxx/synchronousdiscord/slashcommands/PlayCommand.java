package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.lavaplayer.PlayerManager;
import com.larffxx.synchronousdiscord.payload.CommandPayload;
import com.larffxx.synchronousdiscord.payload.MessagePayload;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class PlayCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;
    private final PlayerManager playerManager;
    private String link;

    public PlayCommand(KafkaTemplate<String, MessagePayload> kafkaTemplate, EventReceiver eventReceiver, KafkaTemplate<String, CommandPayload> commandPayloadKafkaTemplate, PlayerManager playerManager, ServersConnectDAO serversConnectDAO) {
        super(kafkaTemplate, eventReceiver, commandPayloadKafkaTemplate);
        this.playerManager = playerManager;
        this.serversConnectDAO = serversConnectDAO;
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
    public void execute(JsonNode data) {
        String[] getLink = data.findValue("command").asText().split(" ");
        link = getLink[1];
        Guild guild = getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild());

        VoiceChannel channel = guild.getVoiceChannelsByName("general", true).get(0);
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);

        playerManager.loadAndPlay(getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0), link);
    }


    @Override
    public String getCommand() {
        return "play";
    }
}
