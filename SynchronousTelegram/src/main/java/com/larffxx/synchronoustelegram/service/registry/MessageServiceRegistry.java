package com.larffxx.synchronoustelegram.service.registry;

import com.larffxx.synchronoustelegram.service.message.MessageService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@Component
@AllArgsConstructor
public class MessageServiceRegistry implements Registry<MessageService> {
    private final Collection<MessageService> messageServices;
    private Map<String, MessageService> messageServiceMap;

    @PostConstruct
    public void init() {
        for (MessageService messageService : messageServices) {
            messageServiceMap.put(messageService.getType(), messageService);
        }
    }

    @Override
    public MessageService get(String t) {
        return messageServiceMap.get(t);
    }
}
