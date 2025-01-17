package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.config.lavaplayer.PlayerManager;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;


@Component
public class PlayCommand implements Command {
    private final ServersConnectDAO serversConnectDAO;
    private final PlayerManager playerManager;
    private final EventReceiver eventReceiver;

    private String link;

    public PlayCommand(PlayerManager playerManager, ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.playerManager = playerManager;
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();

        if (member.getVoiceState().getChannel().asVoiceChannel() == null) {
            event.reply("You are not in a voice channel").queue();
            return;
        }

        link = event.getOption(CommandInfMessages.PLAY_LINK_FROM_DISCORD).getAsString();
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }

        VoiceChannel channel = member.getVoiceState().getChannel().asVoiceChannel();
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);

        playerManager.loadAndPlay(event.getChannel().asTextChannel(), link);
        event.getHook().editOriginal(CommandInfMessages.PLAY_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        link = String.valueOf(data.findValues(CommandInfMessages.PLAY_LINK_FROM_TELEGRAM).get(0).get(0).asText());
        if (!isUrl(link)) {
            link = "ytsearch:" + link;
        }
        Guild guild = eventReceiver.getJda().getGuildById(serversConnectDAO
                .getByTelegramChat(data.findValue(CommandInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild());
        TextChannel textChannel = guild.getTextChannelsByName(CommandInfMessages.DISCORD_TEXT_CHANNEL, true).get(0);

        VoiceChannel channel = guild.getVoiceChannelsByName("general", true).get(0);
        AudioManager manager = guild.getAudioManager();

        manager.openAudioConnection(channel);
        playerManager.loadAndPlay(textChannel, link);
    }

    private boolean isUrl(String url) {
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
