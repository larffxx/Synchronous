package com.larffxx.synchronousdiscord.events;

import com.google.common.base.CaseFormat;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
