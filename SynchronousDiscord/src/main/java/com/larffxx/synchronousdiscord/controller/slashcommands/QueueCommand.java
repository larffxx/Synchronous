package com.larffxx.synchronousdiscord.controller.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.embed.DiscordQueueEmbed;
import com.larffxx.synchronousdiscord.senders.EmbedSender;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;


@Component
public class QueueCommand implements Command {
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;
    private final EmbedSender embedSender;
    private final DiscordQueueEmbed discordQueueEmbed;
    private final EventReceiver eventReceiver;

    public QueueCommand(ResultHandler resultHandler, ServersConnectDAO serversConnectDAO, DiscordQueueEmbed discordQueueEmbed, EventReceiver eventReceiver, EmbedSender embedSender) {
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
        this.discordQueueEmbed = discordQueueEmbed;
        this.eventReceiver = eventReceiver;
        this.embedSender = embedSender;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        queueSender(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        queueSender(musicManager);
    }

    private void queueSender(GuildMusicManager musicManager) {
        EmbedBuilder answer = discordQueueEmbed.createEmbed(musicManager);

        embedSender.send(answer);
    }



    @Override
    public String getCommand() {
        return "queue";
    }


}
