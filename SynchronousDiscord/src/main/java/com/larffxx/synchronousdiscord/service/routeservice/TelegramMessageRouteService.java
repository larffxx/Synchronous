package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.service.registry.SenderRegistry;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService<TelegramMessageContext> {
    private final SenderRegistry senderRegistry;

    public TelegramMessageRouteService(EventContext eventContext, ServersConnectRepository serversConnectRepository, SenderRegistry senderRegistry) {
        super(eventContext, serversConnectRepository);
        this.senderRegistry = senderRegistry;
    }

    //TODO: 1 single route
    public void send(TelegramMessageContext telegramMessageContext) {
        getEventContext().setTextChannel(telegramMessageContext.textChannel());

        Sender sender = senderRegistry.getCommand(String.valueOf(telegramMessageContext.messageType()));

        sender.send(telegramMessageContext);
    }
}


