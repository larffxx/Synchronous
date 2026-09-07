package com.larffxx.synchronousdiscord.service.registry;

import com.larffxx.synchronousdiscord.infrastructure.discord.event.Event;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps Discord event classes to their handlers.
 */
@Component
@RequiredArgsConstructor
public class EventRegistry {
    /**
     * All discovered Discord event handlers.
     */
    private final Collection<Event> events;

    /**
     * Lookup of event handlers by Discord event class.
     */
    private final Map<Class<?>, Event> eventMap = new HashMap<>();

    /**
     * Builds the event class to handler lookup after injection.
     */
    @PostConstruct
    public void mapEvents() {
        for (Event event : events) {
            eventMap.put(event.getEvent(), event);
        }
    }
    /**
     * Returns the handler registered for the given event class.
     *
     * @param event Discord event class to look up
     * @return handler for the given event class
     */
    public Event getCommand(Class event) {
        return eventMap.get(event);
    }
}
