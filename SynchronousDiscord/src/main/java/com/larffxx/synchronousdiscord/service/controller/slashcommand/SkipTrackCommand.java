package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.domain.constants.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SkipTrackCommand implements Command{
    private final ResultHandler resultHandler;
    private final DiscordContextResolveService discordContextResolveService;

    public SkipTrackCommand(ResultHandler resultHandler, DiscordContextResolveService discordContextResolveService) {
        this.resultHandler = resultHandler;
        this.discordContextResolveService = discordContextResolveService;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        GuildMusicManager musicManager = resultHandler.getMusicManager(event.getGuild());
        musicManager.getScheduler().nextTrack();

        event.getHook().editOriginal(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        TextChannel textChannel = context.textChannel();

        context.guildMusicManager().getScheduler().nextTrack();
        textChannel.sendMessage(CommandConstants.SKIP_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "skip";
    }


}
