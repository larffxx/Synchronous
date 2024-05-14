package com.larffxx.synchronousdiscord.routeservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.senders.Sender;
import com.larffxx.synchronousdiscord.senders.SenderPreProcessor;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService<Sender>{


    public TelegramMessageRouteService(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, PreProcessor<Sender> preProcessor) {
        super(eventReceiver, serversConnectDAO, preProcessor);
    }

    public void send(JsonNode data) {
        getEventReceiver().setTextChannel(getEventReceiver().getJda()
                .getGuildById(getServersConnectDAO().getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0));
        if (data.findValue("file").asText().equals("null")) {
            Sender sender = getPreProcessor().getCommand("message");
            sender.send(data);
        } else {
            Sender sender = getPreProcessor().getCommand("fileMessage");
            sender.send(data);
        }
    }
}


