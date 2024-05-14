package com.larffxx.synchronousdiscord.routeservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService<Command>{

    public TelegramCommandRouteService(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, PreProcessor<Command> preProcessor) {
        super(eventReceiver, serversConnectDAO, preProcessor);
    }


    public void send(JsonNode data) {
        getEventReceiver().setTextChannel(getEventReceiver().getJda()
                .getGuildById(getServersConnectDAO().getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram",true).get(0));
        Command command = getPreProcessor().getCommand(data.findValue("command").asText());
        command.execute(data);
    }

}
