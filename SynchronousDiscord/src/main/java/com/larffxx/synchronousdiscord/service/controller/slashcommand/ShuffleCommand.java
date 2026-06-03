package com.larffxx.synchronousdiscord.service.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.domain.exception.command.ShuffleException;
import com.larffxx.synchronousdiscord.domain.record.DiscordContext;
import com.larffxx.synchronousdiscord.infrastructure.DiscordContextResolver;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.CommandConstants;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

@Service
public class ShuffleCommand implements Command{
    private final ResultHandler resultHandler;
    private final DiscordContextResolver discordContextResolver;

    public ShuffleCommand(ResultHandler resultHandler, DiscordContextResolver discordContextResolver) {
        this.resultHandler = resultHandler;
        this.discordContextResolver = discordContextResolver;
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws ShuffleException {
        GuildMusicManager manager = resultHandler.getMusicManager(t.getGuild());

        manager.getScheduler().shuffle();

        t.getHook().editOriginal(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) throws ShuffleException {
        DiscordContext context = discordContextResolver.resolveContext(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());

        TextChannel textChannel = context.textChannel();
        context.guildMusicManager().getScheduler().shuffle();

        textChannel.sendMessage(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "shuffle";
    }
}
