package com.larffxx.synchronousdiscord.senders;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService{
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;
    private final SenderPreProcessor senderPreProcessor;

    public TelegramMessageRouteService(ServersConnectDAO serversConnectDAO, EventReceiver eventReceiver, SenderPreProcessor senderPreProcessor) {
        this.serversConnectDAO = serversConnectDAO;
        this.eventReceiver = eventReceiver;
        this.senderPreProcessor = senderPreProcessor;
    }


    public void send(JsonNode data) {
        eventReceiver.setTextChannel(eventReceiver.getJda()
                .getGuildById(serversConnectDAO.getByTelegramChat(data.findValue("chatId").asText()).getDiscordGuild())
                .getTextChannelsByName("telegram", true).get(0));
        if (data.findValue("file").asText().equals("null")) {
            Sender sender = senderPreProcessor.getCommand("message");
            sender.send(data);
        } else {
            Sender sender = senderPreProcessor.getCommand("fileMessage");
            sender.send(data);
        }
    }
}


