package com.larffxx.synchronousdiscord.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.constant.infmsg.SendersConstants;
import com.larffxx.synchronousdiscord.service.registry.SenderRegistry;
import com.larffxx.synchronousdiscord.infrastructure.discord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService{
    private final SenderRegistry senderRegistry;

    public TelegramMessageRouteService(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository, SenderRegistry senderRegistry) {
        super(eventReceiver, serversConnectRepository);
        this.senderRegistry = senderRegistry;
    }

    public void send(JsonNode data) {
        getEventReceiver().setTextChannel(getEventReceiver()
                .getJda()
                .getGuildById(getServersConnectRepository()
                        .getConnectByTelegramChannel(data.findValue(SendersConstants.TELEGRAM_CHAT_ID).asText())
                        .getDiscordGuild())
                .getTextChannelsByName(SendersConstants.TEXT_CHANNEL_IN_DISCORD, true)
                .get(0));
        Sender sender = senderRegistry.getCommand(data.findValue(SendersConstants.MESSAGE_TYPE).asText());

        sender.send(data);
    }
}


