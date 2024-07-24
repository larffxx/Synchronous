package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.listeners.CommandListener;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
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
    public final ResultHandler resultHandler;

    public QueueCommand(EventReceiver eventReceiver, CommandListener commandListener, ResultHandler resultHandler, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.commandListener = commandListener;
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        queueEmbedCreator(musicManager);

        event.reply("Current queue").queue();
    }


    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(getGUILD_ID_FROM_TELEGRAM()).asText()).getDiscordGuild();
        Guild guild = getEventReceiver().getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        queueEmbedCreator(musicManager);
    }

    private void queueEmbedCreator(GuildMusicManager musicManager) {
        List<AudioTrack> queue = new ArrayList<>(musicManager.getScheduler().getQueue());
        EmbedBuilder eb = new EmbedBuilder();
        if (queue.isEmpty()) {
            eb.setDescription("Queue is empty");
        }else {
            for (int i = 0; i < 10; i++) {
                eb.addField(i + ":", queue.get(i).getInfo().title, false);
            }
        }
        commandListener.getEmbedSender().send(eb);
    }


    @Override
    public String getCommand() {
        return "queue";
    }



}
