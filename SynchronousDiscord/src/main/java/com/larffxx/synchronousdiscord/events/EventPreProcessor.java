package com.larffxx.synchronousdiscord.events;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventPreProcessor {
    private final Collection<Event> events;

    private final Map<Class<?>, Event> eventMap = new HashMap<>();

    @PostConstruct
    public void mapEvents() {
        for (Event event : events) {
            eventMap.put(event.getEvent(), event);
        }
    }

    public Event getCommand(Class event) {
        return eventMap.get(event);
    }
}
