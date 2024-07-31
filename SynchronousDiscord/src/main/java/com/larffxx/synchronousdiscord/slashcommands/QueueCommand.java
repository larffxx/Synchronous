package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.utility.QueueChecker;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class QueueCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;
    private final CommandListener commandListener;
    private final ResultHandler resultHandler;
    private final QueueChecker queueChecker;

    public QueueCommand(EventReceiver eventReceiver, CommandListener commandListener, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO, QueueChecker queueChecker) {
        super(eventReceiver);
        this.commandListener = commandListener;
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
        this.queueChecker = queueChecker;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        queueSender(musicManager);

        event.reply(CommandInfMessages.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandInfMessages.GUILD_ID_FROM_TELEGRAM).asText()).getDiscordGuild();
        Guild guild = getEventReceiver().getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        queueSender(musicManager);
    }

    private void queueSender(GuildMusicManager musicManager) {
        List<AudioTrack> queue = new ArrayList<>(musicManager.getScheduler().getQueue());
        EmbedBuilder eb = new EmbedBuilder();

        queueChecker.queueCheck(queue, eb);

        commandListener.getEmbedSender().send(eb);
    }



    @Override
    public String getCommand() {
        return "queue";
    }


}
