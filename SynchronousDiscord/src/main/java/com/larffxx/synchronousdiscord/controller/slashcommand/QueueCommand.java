package com.larffxx.synchronousdiscord.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.embed.creator.QueueEmbedCreator;
import com.larffxx.synchronousdiscord.sender.embed.EmbedSender;
import com.larffxx.synchronousdiscord.service.QueueEmbedService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;


@Component
public class QueueCommand implements Command {
    private final ServersConnectDAO serversConnectDAO;
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final QueueEmbedService queueEmbedService;

    public QueueCommand(ResultHandler resultHandler, ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver, EmbedSender embedSender, QueueEmbedService queueEmbedService) {
        this.resultHandler = resultHandler;
        this.serversConnectDAO = serversConnectDAO;
        this.queueEmbedService = queueEmbedService;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());

        queueEmbedService.sendQueueEmbed(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(JsonNode data) {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);

        GuildMusicManager musicManager = resultHandler.getMusicManager(guild);

        queueEmbedService.sendQueueEmbed(musicManager);
    }

    @Override
    public String getCommand() {
        return "queue";
    }


}
