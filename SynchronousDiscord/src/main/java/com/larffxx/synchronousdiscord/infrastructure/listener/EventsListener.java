package com.larffxx.synchronousdiscord.infrastructure.listener;

import com.larffxx.synchronousdiscord.infrastructure.discord.event.Event;
import com.larffxx.synchronousdiscord.service.registry.EventRegistry;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EventsListener implements EventListener {

    private final EventRegistry eventRegistry;

    public EventsListener(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (eventRegistry.getCommand(event.getClass()) != null) {
            Event ev = eventRegistry.getCommand(event.getClass());

            ev.execute(event);
        }
    }
}
