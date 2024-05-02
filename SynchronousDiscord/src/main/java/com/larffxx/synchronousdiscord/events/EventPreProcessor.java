package com.larffxx.synchronousdiscord.events;

import com.google.common.base.CaseFormat;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.GenericEvent;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@AllArgsConstructor
public class EventPreProcessor implements PreProcessor<Event<GenericEvent>> {
    private final Collection<Event> events;

    private Map<String, Event> eventMap;

    @PostConstruct
    public void mapEvents() {
        for (Event event : events) {
            eventMap.put(event.getEvent(), event);
        }
    }

    @Override
    public Event getCommand(String event) {
        return eventMap.get(event);
    }

    public Event getCommand(GenericEvent genericEvent) {
        return eventMap.get(genericEvent);
    }

    public static String getFormatedString(GenericEvent event) {
        String[] cc = event.toString().split("(id=)");
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, cc[0].replace("(", ""));
    }
}
