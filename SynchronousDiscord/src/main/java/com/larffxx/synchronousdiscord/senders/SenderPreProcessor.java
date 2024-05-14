package com.larffxx.synchronousdiscord.senders;

import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class SenderPreProcessor implements PreProcessor<Sender> {
    private final Collection<Sender> senders;

    private Map<String, Sender> senderMap;

    @PostConstruct
    public void mapCommands() {
        for (Sender sender : senders) {
            senderMap.put(sender.getSender(), sender);
        }
    }
    @Override
    public Sender<?> getCommand(String command) {
        return senderMap.get(command);
    }
}
