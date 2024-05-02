package com.larffxx.synchronousdiscord.events;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Event<T> implements EventInterface<T> {
    private final EventReceiver eventReceiver;

    public Event(EventReceiver eventReceiver) {
        this.eventReceiver = eventReceiver;
    }

}
