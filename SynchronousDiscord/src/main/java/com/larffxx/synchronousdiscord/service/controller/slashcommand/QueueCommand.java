package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.DiscordAudioContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.service.QueueEmbedService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;


@Service
public class QueueCommand implements Command {
    private final ResultHandler resultHandler;
    private final QueueEmbedService queueEmbedService;
    private final DiscordContextResolver discordContextResolver;

    public QueueCommand(ResultHandler resultHandler, QueueEmbedService queueEmbedService, DiscordContextResolver discordContextResolver) {
        this.resultHandler = resultHandler;
        this.queueEmbedService = queueEmbedService;
        this.discordContextResolver = discordContextResolver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());

        queueEmbedService.sendQueueEmbed(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        DiscordAudioContext context = discordContextResolver.resolveContext(telegramCommandContext);

        queueEmbedService.sendQueueEmbed(context.guildMusicManager());
    }

    @Override
    public String getCommand() {
        return "queue";
    }


}
