package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.message.MessageSender;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;


/**
 * Routes incoming Telegram messages to the Discord message sender.
 */
@Component
public class TelegramMessageRouteService extends RouteService<TelegramMessageContext> {
    /**
     * Sender used to deliver messages to Discord.
     */
    private final MessageSender messageSender;

    /**
     * Creates a route service for Telegram messages.
     *
     * @param eventContext shared event context
     * @param serversConnectRepository repository for server connections
     * @param messageSender sender for Discord messages
     */
    public TelegramMessageRouteService(EventContext eventContext, ServersConnectRepository serversConnectRepository, MessageSender messageSender) {
        super(eventContext, serversConnectRepository);
        this.messageSender = messageSender;
    }

    /**
     * Sets the target channel and forwards the Telegram message to Discord.
     *
     * @param telegramMessageContext Telegram message context to route
     */
    public void send(TelegramMessageContext telegramMessageContext) {
        getEventContext().setTextChannel(telegramMessageContext.textChannel());

        messageSender.sendMessage(telegramMessageContext);
    }
}


