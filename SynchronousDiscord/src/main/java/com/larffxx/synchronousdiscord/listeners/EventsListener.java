package com.larffxx.synchronousdiscord.listeners;

import com.larffxx.synchronousdiscord.events.Event;
import com.larffxx.synchronousdiscord.events.EventPreProcessor;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EventsListener implements EventListener {

    private final EventPreProcessor eventPreProcessor;

    public EventsListener(EventPreProcessor eventPreProcessor) {
        this.eventPreProcessor = eventPreProcessor;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (eventPreProcessor.getCommand(event.getClass()) != null) {
            Event ev = eventPreProcessor.getCommand(event.getClass());
            ev.execute(event);
        }
    }
}
