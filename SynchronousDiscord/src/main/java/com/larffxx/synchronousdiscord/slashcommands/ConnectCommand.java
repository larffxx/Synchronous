package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.model.ServersConnect;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ConnectCommand implements Command{
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    @Override
    public void execute(SlashCommandInteractionEvent event) {
        serversConnectDAO.saveServer(event.getGuild().getId(), event.getOption(CommandInfMessages.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString());
        event.getHook().editOriginal(CommandInfMessages.CONNECT_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        ServersConnect serversConnect = serversConnectDAO.getByTelegramChat(data.findValue(CommandInfMessages.TELEGRAM_CHAT_ID).asText());
        Guild guildId = eventReceiver.getJda().getGuildById(serversConnect.getDiscordGuild());
        TextChannel textChannel = guildId.getTextChannelsByName(CommandInfMessages.DISCORD_TEXT_CHANNEL,true).get(0);

        textChannel.sendMessage(CommandInfMessages.CONNECT_SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
