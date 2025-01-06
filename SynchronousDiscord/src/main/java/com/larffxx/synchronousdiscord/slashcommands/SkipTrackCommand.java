package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SkipTrackCommand implements Command{
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;

    public SkipTrackCommand(ResultHandler resultHandler, ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();

        event.getHook().editOriginal(CommandInfMessages.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);
        TextChannel textChannel = guild.getTextChannelsByName(CommandInfMessages.TELEGRAM_CHANNEL, true).get(0);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        musicManager.getScheduler().nextTrack();
        textChannel.sendMessage(CommandInfMessages.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "skip";
    }


}
