package com.larffxx.synchronousdiscord.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService<Sender>{

    public TelegramMessageRouteService(EventReceiver eventReceiver, PreProcessor<Sender> preProcessor, ServersConnectRepository serversConnectRepository) {
        super(eventReceiver, preProcessor, serversConnectRepository);
    }

    public void send(JsonNode data) {
        getEventReceiver().setTextChannel(getEventReceiver()
                .getJda()
                .getGuildById(getServersConnectRepository()
                        .getConnectByTelegramChannel(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText())
                        .getDiscordGuild())
                .getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true)
                .get(0));
        Sender sender = getPreProcessor().getCommand(data.findValue(SendersConstants.MESSAGE_TYPE).asText());

        sender.send(data);
    }
}


