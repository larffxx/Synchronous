package com.larffxx.synchronousdiscord.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.sender.Sender;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService<Sender>{


    public TelegramMessageRouteService(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, PreProcessor<Sender> preProcessor) {
        super(eventReceiver, serversConnectDAO, preProcessor);
    }

    public void send(JsonNode data) {
        getEventReceiver().setTextChannel(getEventReceiver()
                .getJda()
                .getGuildById(getServersConnectDAO()
                        .getByTelegramChat(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText())
                        .getDiscordGuild())
                .getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true)
                .get(0));
        Sender sender = getPreProcessor().getCommand(data.findValue(SendersConstants.MESSAGE_TYPE).asText());

        sender.send(data);
    }
}


