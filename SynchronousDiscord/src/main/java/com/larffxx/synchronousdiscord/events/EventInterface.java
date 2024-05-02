package com.larffxx.synchronousdiscord.events;


import org.springframework.stereotype.Component;

@Component
public interface EventInterface<T> {
    void execute(T event);

    String getEvent();
}
