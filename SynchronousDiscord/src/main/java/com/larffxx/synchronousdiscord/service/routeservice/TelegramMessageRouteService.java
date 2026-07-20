package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.message.MessageSender;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;


@Component
public class TelegramMessageRouteService extends RouteService<TelegramMessageContext> {
    private final MessageSender messageSender;

    public TelegramMessageRouteService(EventContext eventContext, ServersConnectRepository serversConnectRepository, MessageSender messageSender) {
        super(eventContext, serversConnectRepository);
        this.messageSender = messageSender;
    }

    public void send(TelegramMessageContext telegramMessageContext) {
        getEventContext().setTextChannel(telegramMessageContext.textChannel());

        messageSender.sendMessage(telegramMessageContext);
    }
}


