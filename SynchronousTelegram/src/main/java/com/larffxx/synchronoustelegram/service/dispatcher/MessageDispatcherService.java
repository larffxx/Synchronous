package com.larffxx.synchronoustelegram.service.dispatcher;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.handler.message.MessageHandler;
import com.larffxx.synchronoustelegram.service.utility.MessageDefineService;
import com.larffxx.synchronoustelegram.service.registry.MessageHandlerRegistry;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Dispatches incoming messages to the matching message handler.
 */
@Service
public class MessageDispatcherService {
    /**
     * Service that classifies raw updates by message type.
     */
    private final MessageDefineService messageDefineService;
    /**
     * Registry of message handlers keyed by message type.
     */
    private final MessageHandlerRegistry messagePreProcessor;

    /**
     * Creates a dispatcher with its dependencies.
     *
     * @param messageDefineService service that classifies raw updates
     * @param messagePreProcessor registry of message handlers
     */
    public MessageDispatcherService(MessageDefineService messageDefineService, MessageHandlerRegistry messagePreProcessor) {
        this.messageDefineService = messageDefineService;
        this.messagePreProcessor = messagePreProcessor;
    }

    /**
     * Classifies a raw update and forwards it to the matching handler.
     *
     * @param update raw Telegram update to dispatch
     */
    public void dispatch(Update update){
        String messageType = messageDefineService.defineMessage(update);

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(update);
    }

    /**
     * Forwards an already classified message context to the matching handler.
     *
     * @param messageContext classified message context to dispatch
     */
    public void dispatch(MessageContext messageContext){
        String messageType = messageContext.getMessageType().toString();

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(messageContext);
    }
}
