package com.larffxx.synchronousdiscord.service.registry;

import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Maps sender names to message sender handlers.
 */
@Component
@AllArgsConstructor
public class SenderRegistry implements Registry<Sender> {
    /**
     * All discovered message senders.
     */
    private final Collection<Sender> senders;

    /**
     * Lookup of senders by sender name.
     */
    private Map<String, Sender> senderMap;

    /**
     * Builds the sender name to handler lookup after injection.
     */
    @PostConstruct
    public void mapCommands() {
        for (Sender sender : senders) {
            senderMap.put(sender.getSender(), sender);
        }
    }
    /**
     * Returns the sender registered under the given name.
     *
     * @param command registered sender name
     * @return sender for the given name
     */
    @Override
    public Sender<?> getSender(String command) {
        return senderMap.get(command);
    }
}
