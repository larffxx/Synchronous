package com.larffxx.synchronousdiscord.slashcommands;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
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
public class ConnectCommand extends Command{
    private final ServersConnectDAO serversConnectDAO;

    private final String TELEGRAM_CHANNEL_NAME = "connect";
    private final String SUCCESS_MESSAGE = "Servers connected successfully!";
    private final String TELEGRAM_CHANNEL = "telegram";

    public ConnectCommand(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO) {
        super(eventReceiver);
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(SlashCommandInteractionEvent t) {
        serversConnectDAO.saveServer(t.getGuild().getId(), t.getOption(TELEGRAM_CHANNEL_NAME).getAsString());
        t.reply(SUCCESS_MESSAGE).queue();
    }

    @Override
    public void execute(JsonNode data) {
        ServersConnect serversConnect = serversConnectDAO.getByTelegramChat(data.findValue(getGUILD_ID_FROM_TELEGRAM()).asText());
        Guild guildId = getEventReceiver().getJda().getGuildById(serversConnect.getDiscordGuild());
        TextChannel textChannel = guildId.getTextChannelsByName(TELEGRAM_CHANNEL,true).get(0);

        textChannel.sendMessage(SUCCESS_MESSAGE).queue();
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
