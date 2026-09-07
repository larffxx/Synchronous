package com.larffxx.synchronoustelegram.service.routeservice;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Routes messages received from Discord to the Telegram dispatcher.
 */
@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    /**
     * Dispatcher that forwards messages to the matching handler.
     */
    private final MessageDispatcherService messageDispatcherService;

    /**
     * Creates a route service with its dependencies.
     *
     * @param messageDispatcherService dispatcher for incoming messages
     */
    public DiscordMessageRouteService(MessageDispatcherService messageDispatcherService) {
        this.messageDispatcherService = messageDispatcherService;
    }

    /**
     * Forwards a Discord originated message to Telegram for dispatch.
     *
     * @param messageContext classified message context to dispatch
     */
    public void send(MessageContext messageContext) {
        messageDispatcherService.dispatch(messageContext);
    }
}
