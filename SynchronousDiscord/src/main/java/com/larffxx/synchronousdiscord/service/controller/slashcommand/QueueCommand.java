package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.domain.constants.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.service.QueueEmbedService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;


@Component
public class QueueCommand implements Command {
    private final ResultHandler resultHandler;
    private final QueueEmbedService queueEmbedService;
    private final DiscordContextResolveService discordContextResolveService;

    public QueueCommand(ResultHandler resultHandler, QueueEmbedService queueEmbedService, DiscordContextResolveService discordContextResolveService) {
        this.resultHandler = resultHandler;
        this.queueEmbedService = queueEmbedService;
        this.discordContextResolveService = discordContextResolveService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());

        queueEmbedService.sendQueueEmbed(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
    }


    @Override
    public void execute(JsonNode data) {
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        queueEmbedService.sendQueueEmbed(context.guildMusicManager());
    }

    @Override
    public String getCommand() {
        return "queue";
    }


}
