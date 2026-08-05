package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.larffxx.synchronousdiscord.domain.context.DiscordAudioContext;
import com.larffxx.synchronousdiscord.domain.context.EmbedContext;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.service.embed.EmbedService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;


@Service
public class QueueCommand implements Command {
    private final ResultHandler resultHandler;
    private final DiscordContextResolver discordContextResolver;
    private final EmbedService embedService;

    public QueueCommand(ResultHandler resultHandler, DiscordContextResolver discordContextResolver, EmbedService embedService) {
        this.resultHandler = resultHandler;
        this.discordContextResolver = discordContextResolver;
        this.embedService = embedService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());

        EmbedContext embedContext = embedService.getEmbedContext(musicManager);

        event.getHook().editOriginal(CommandConstants.QUEUE_SUCCESS_MESSAGE).queue();
        event.getHook().editOriginalEmbeds(embedContext.embedBuilder().build()).queue();
    }


    @Override
    public void execute(TelegramCommandContext telegramCommandContext) {
        DiscordAudioContext context = discordContextResolver.resolveContext(telegramCommandContext);

        EmbedContext embedContext = embedService.getEmbedContext(context.guildMusicManager());

        embedService.sendEmbed(embedContext);
    }

    @Override
    public String getCommand() {
        return "queue";
    }


}
