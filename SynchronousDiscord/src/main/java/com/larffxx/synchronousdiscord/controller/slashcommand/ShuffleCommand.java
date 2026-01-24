package com.larffxx.synchronousdiscord.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.exception.command.ShuffleException;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.repo.ServersConnectRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class ShuffleCommand implements Command{
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;
    public ShuffleCommand(ResultHandler resultHandler, EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws ShuffleException {
        GuildMusicManager manager = resultHandler.getMusicManager(t.getGuild());

        manager.getScheduler().shuffle();

        t.getHook().editOriginal(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) throws ShuffleException {
        String guildId = serversConnectRepository.getConnectByTelegramChannel(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
        Guild guild = eventReceiver.getJda().getGuildById(guildId);
        TextChannel textChannel = guild.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL, true).get(0);

        GuildMusicManager manager = resultHandler.getMusicManager(guild);
        manager.getScheduler().shuffle();

        textChannel.sendMessage(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public String getCommand() {
        return "shuffle";
    }
}
