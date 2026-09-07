package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.message.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Registry that looks up message services by message type.
 */
@Getter
@Setter
@Component
@AllArgsConstructor
public class MessageServiceRegistry implements Registry<MessageService> {
    /**
     * All message service beans discovered by Spring.
     */
    private final Collection<MessageService> messageServices;
    /**
     * Service lookup map keyed by message type.
     */
    private Map<String, MessageService> messageServiceMap;

    /**
     * Indexes all message services by their type after construction.
     */
    @PostConstruct
    public void init() {
        for (MessageService messageService : messageServices) {
            messageServiceMap.put(messageService.getType(), messageService);
        }
    }

    /**
     * Returns the message service registered for the given type.
     *
     * @param t message type to look up
     * @return matching service or null if none is registered
     */
    @Override
    public MessageService get(String t) {
        return messageServiceMap.get(t);
    }
}
