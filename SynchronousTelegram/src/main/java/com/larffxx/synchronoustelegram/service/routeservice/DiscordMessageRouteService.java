package com.larffxx.synchronoustelegram.service.routeservice;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordMessageRouteService {
    private final MessageDispatcherService messageDispatcherService;
    private final TextMessageService textMessageService;

    public DiscordMessageRouteService(MessageDispatcherService messageDispatcherService, TextMessageService textMessageService) {
        this.messageDispatcherService = messageDispatcherService;
        this.textMessageService = textMessageService;
    }

    public void send(MessageContext messageContext) {
        messageDispatcherService.dispatch(messageContext);
    }
}
