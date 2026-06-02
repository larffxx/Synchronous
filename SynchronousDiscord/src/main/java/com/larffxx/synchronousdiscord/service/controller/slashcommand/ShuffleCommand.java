package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.exception.command.ShuffleException;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.service.DiscordContextResolveService;
import com.larffxx.synchronousdiscord.domain.constants.infmsg.CommandConstants;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class ShuffleCommand implements Command{
    private final ResultHandler resultHandler;
    private final DiscordContextResolveService discordContextResolveService;

    public ShuffleCommand(ResultHandler resultHandler, DiscordContextResolveService discordContextResolveService) {
        this.resultHandler = resultHandler;
        this.discordContextResolveService = discordContextResolveService;
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws ShuffleException {
        GuildMusicManager manager = resultHandler.getMusicManager(t.getGuild());

        manager.getScheduler().shuffle();

        t.getHook().editOriginal(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) throws ShuffleException {
        DiscordContext context = discordContextResolveService.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        TextChannel textChannel = context.textChannel();
        context.guildMusicManager().getScheduler().shuffle();

        textChannel.sendMessage(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "shuffle";
    }
}
