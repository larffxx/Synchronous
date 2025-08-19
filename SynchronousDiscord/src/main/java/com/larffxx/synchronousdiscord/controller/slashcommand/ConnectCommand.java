package com.larffxx.synchronousdiscord.controller.slashcommand;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.CommandConstants;
import com.larffxx.synchronousdiscord.model.ServersConnect;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
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
        serversConnectDAO.saveServer(event.getGuild().getId(), event.getOption(CommandConstants.TELEGRAM_CHANNEL_NAME_FROM_OPTIONS).getAsString());
        event.getHook().editOriginal(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        ServersConnect serversConnect = serversConnectDAO.getByTelegramChat(data.findValue(CommandConstants.TELEGRAM_CHAT_ID).asText());
        Guild guildId = eventReceiver.getJda().getGuildById(serversConnect.getDiscordGuild());
        TextChannel textChannel = guildId.getTextChannelsByName(CommandConstants.DISCORD_TEXT_CHANNEL,true).get(0);

        textChannel.sendMessage(CommandConstants.CONNECT_SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
