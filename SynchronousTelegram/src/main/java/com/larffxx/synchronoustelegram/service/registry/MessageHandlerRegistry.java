package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.infrastructure.handler.message.MessageHandler;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Registry that looks up message handlers by message type.
 */
@Component
@AllArgsConstructor
public class MessageHandlerRegistry implements Registry<MessageHandler> {
    /**
     * All message handler beans discovered by Spring.
     */
    private final Collection<MessageHandler> messageHandlerTypes;
    /**
     * Handler lookup map keyed by message type.
     */
    private Map<String, MessageHandler> messageMap;

    /**
     * Indexes all handlers by their type after construction.
     */
    @PostConstruct
    public void init() {
        for(MessageHandler messageHandler : messageHandlerTypes){
            messageMap.put(messageHandler.getType(), messageHandler);
        }
    }

    /**
     * Returns the handler registered for the given message type.
     *
     * @param message message type to look up
     * @return matching handler or null if none is registered
     */
    @Override
    public MessageHandler get(String message) {
        return messageMap.get(message);
    }
}
