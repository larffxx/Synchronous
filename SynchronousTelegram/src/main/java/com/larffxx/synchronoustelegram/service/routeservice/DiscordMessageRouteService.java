package com.larffxx.synchronoustelegram.service.routeservice;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

import com.larffxx.synchronoustelegram.service.message.TextMessageService;
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
     * Service for sending text messages to Telegram chats.
     */
    private final TextMessageService textMessageService;

    /**
     * Creates a route service with its dependencies.
     *
     * @param messageDispatcherService dispatcher for incoming messages
     * @param textMessageSender service for sending text messages
     */
    public DiscordMessageRouteService(MessageDispatcherService messageDispatcherService, TextMessageService textMessageService) {
        this.messageDispatcherService = messageDispatcherService;
        this.textMessageService = textMessageService;
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
