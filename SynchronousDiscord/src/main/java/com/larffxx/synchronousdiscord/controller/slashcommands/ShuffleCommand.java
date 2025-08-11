package com.larffxx.synchronousdiscord.controller.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.config.lavaplayer.GuildMusicManager;
import com.larffxx.synchronousdiscord.config.lavaplayer.ResultHandler;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
public class ShuffleCommand implements Command{
    private final ResultHandler resultHandler;
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;

    public ShuffleCommand(ResultHandler resultHandler, EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO) {
        this.resultHandler = resultHandler;
        this.eventReceiver = eventReceiver;
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void execute(SlashCommandInteractionEvent t) throws CommandException {
        GuildMusicManager manager = resultHandler.getMusicManager(t.getGuild());

        manager.getScheduler().shuffle();

        t.getHook().editOriginal(CommandConstants.SHUFFLE_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) throws CommandException {
        String guildId = serversConnectDAO.getByTelegramChat(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText()).getDiscordGuild();
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
