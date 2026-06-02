package com.larffxx.synchronoustelegram.service.dispatcher;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.handler.message.MessageHandler;
import com.larffxx.synchronoustelegram.service.handler.utility.MessageDefiner;
import com.larffxx.synchronoustelegram.service.registry.MessageHandlerRegistry;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageDispatcher {
    private final MessageDefiner messageDefiner;
    private final MessageHandlerRegistry messagePreProcessor;

    public MessageDispatcher(MessageDefiner messageDefiner, MessageHandlerRegistry messagePreProcessor) {
        this.messageDefiner = messageDefiner;
        this.messagePreProcessor = messagePreProcessor;
    }

    public void dispatch(Update update){
        String messageType = messageDefiner.defineMessage(update);

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(update);
    }

    public void dispatch(DiscordPayload discordPayload){
        String messageType = discordPayload.getMessageType().toString();

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(discordPayload);
    }
}
