package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class SkipTrackCommand implements Command{
    private final ResultHandler resultHandler;
    private final DiscordContextResolver discordContextResolver;

    public SkipTrackCommand(ResultHandler resultHandler, DiscordContextResolver discordContextResolver) {
        this.resultHandler = resultHandler;
        this.discordContextResolver = discordContextResolver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();

        event.getHook().editOriginal(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        DiscordContext context = discordContextResolver.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().nextTrack();
        textChannel.sendMessage(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "skip";
    }


}
