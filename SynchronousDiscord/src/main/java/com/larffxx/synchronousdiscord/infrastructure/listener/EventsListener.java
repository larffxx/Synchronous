package com.larffxx.synchronousdiscord.infrastructure.listener;

import com.larffxx.synchronousdiscord.infrastructure.discord.event.Event;
import com.larffxx.synchronousdiscord.service.registry.EventRegistry;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;

/**
 * Events Listener class.
 */
@Component
@Getter
@Setter
public class EventsListener implements EventListener {

    /**
     * The event registry.
     */
    private final EventRegistry eventRegistry;

    /**
     * Creates a new EventsListener.
     * @param eventRegistry the event registry.
     */
    public EventsListener(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    /**
     * Dispatches incoming gateway event to its handler.
     * @param event the event.
     */
    @Override
    public void onEvent(GenericEvent event) {
        Event ev = eventRegistry.getCommand(event.getClass());
        if (ev != null) {
            ev.execute(event);
        }
    }
}
