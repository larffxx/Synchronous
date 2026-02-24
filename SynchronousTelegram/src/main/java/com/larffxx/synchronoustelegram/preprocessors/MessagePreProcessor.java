package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.application.handler.MessageHandler;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public class MessagePreProcessor implements PreProcessor<MessageHandler> {
    private final Collection<MessageHandler> messageHandlerTypes;
    private Map<String, MessageHandler> messageMap;

    @PostConstruct
    public void init() {
        for(MessageHandler messageHandler : messageHandlerTypes){
            messageMap.put(messageHandler.getType(), messageHandler);
        }
    }

    @Override
    public MessageHandler get(String message) {
        return messageMap.get(message);
    }

    @Override
    public MessageHandler get(UpdateReceiver updateReceiver) {
        return messageMap.get(updateReceiver.getUpdate().getMessage());
    }

    public MessagePreProcessor(Collection<MessageHandler> messageHandlerTypes, final Map<String, MessageHandler> messageMap) {
        this.messageHandlerTypes = messageHandlerTypes;
        this.messageMap = messageMap;
    }
}
