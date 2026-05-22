package com.larffxx.synchronousdiscord.application.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.mapper.ServersConnectMapper;
import com.larffxx.synchronousdiscord.domain.model.ServersConnect;
import com.larffxx.synchronousdiscord.domain.record.DiscordMusicContext;
import com.larffxx.synchronousdiscord.domain.service.DiscordMusicService;
import com.larffxx.synchronousdiscord.dto.ServersConnectDTO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.embed.EmbedSender;
import com.larffxx.synchronousdiscord.domain.service.QueueEmbedService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;


@Component
public class QueueCommand implements Command {
    private final ResultHandler resultHandler;
    private final QueueEmbedService queueEmbedService;
    private final DiscordMusicService discordMusicService;

    public QueueCommand(ResultHandler resultHandler, QueueEmbedService queueEmbedService, DiscordMusicService discordMusicService) {
        this.resultHandler = resultHandler;
        this.queueEmbedService = queueEmbedService;
        this.discordMusicService = discordMusicService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());

        queueEmbedService.sendQueueEmbed(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(JsonNode data) {
        DiscordMusicContext context = discordMusicService.resolveMusicContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        queueEmbedService.sendQueueEmbed(context.guildMusicManager());
    }

    @Override
    public String getCommand() {
        return "queue";
    }


}
