package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.handler.message.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class MessageHandlerRegistry implements Registry<MessageHandler> {
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
}
