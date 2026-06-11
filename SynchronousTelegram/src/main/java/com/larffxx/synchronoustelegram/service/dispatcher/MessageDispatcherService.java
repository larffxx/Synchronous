package com.larffxx.synchronoustelegram.service.dispatcher;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.handler.message.MessageHandler;
import com.larffxx.synchronoustelegram.service.utility.MessageDefineService;
import com.larffxx.synchronoustelegram.service.registry.MessageHandlerRegistry;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageDispatcherService {
    private final MessageDefineService messageDefineService;
    private final MessageHandlerRegistry messagePreProcessor;

    public MessageDispatcherService(MessageDefineService messageDefineService, MessageHandlerRegistry messagePreProcessor) {
        this.messageDefineService = messageDefineService;
        this.messagePreProcessor = messagePreProcessor;
    }

    public void dispatch(Update update){
        String messageType = messageDefineService.defineMessage(update);

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(update);
    }

    public void dispatch(MessageContext messageContext){
        String messageType = messageContext.getMessageType().toString();

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(messageContext);
    }
}
