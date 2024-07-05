package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.lavaplayer.PlayerManager;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Component
public class PlayCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;
    private final PlayerManager playerManager;
    private String link;

    public PlayCommand(EventReceiver eventReceiver, PlayerManager playerManager, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.playerManager = playerManager;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (!event.getUser().isBot()) {
            link = event.getOption("type").getAsString();
            if (!isUrl(link)) {
                link = "ytsearch:" + link;
            }
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
        link = String.valueOf(data.findValues("options").get(0).get(0).asText());
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }
        Guild guild = getEventReceiver().getJda().getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("guildId").asText()).getDiscordGuild());

        VoiceChannel channel = guild.getVoiceChannelsByName("general", true).get(0);
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);

        playerManager.loadAndPlay(getEventReceiver().getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("guildId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0), link);
    }

    public boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


    @Override
    public String getCommand() {
        return "play";
    }
}
