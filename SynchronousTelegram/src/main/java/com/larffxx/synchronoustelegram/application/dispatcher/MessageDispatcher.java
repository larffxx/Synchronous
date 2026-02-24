package com.larffxx.synchronoustelegram.application.dispatcher;

import com.larffxx.synchronoustelegram.application.handler.MessageHandler;
import com.larffxx.synchronoustelegram.application.handler.utility.MessageDefiner;
import com.larffxx.synchronoustelegram.preprocessors.MessagePreProcessor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageDispatcher {
    private final MessageDefiner messageDefiner;
    private final MessagePreProcessor messagePreProcessor;

    public MessageDispatcher(MessageDefiner messageDefiner, MessagePreProcessor messagePreProcessor) {
        this.messageDefiner = messageDefiner;
        this.messagePreProcessor = messagePreProcessor;
    }

    public void dispatch(Update update){
        String messageType = messageDefiner.defineMessage(update);

        MessageHandler messageHandler = messagePreProcessor.get(messageType);

        messageHandler.handle(update);
    }
}
