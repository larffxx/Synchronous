package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import com.larffxx.synchronousdiscord.slashcommands.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService  {
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;
    private final CommandPreProcessor commandPreProcessor;

    public TelegramCommandRouteService(CommandPreProcessor commandPreProcessor, ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver) {
        this.commandPreProcessor = commandPreProcessor;
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
    }


    public void send(JsonNode data) {
        eventReceiver.setTextChannel(eventReceiver.getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram",true).get(0));
        Command command = commandPreProcessor.getCommand(data.findValue("command").asText());
        command.execute(data);
    }

}
