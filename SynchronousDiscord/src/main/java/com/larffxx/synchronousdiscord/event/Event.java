package com.larffxx.synchronousdiscord.event;

import org.springframework.stereotype.Component;

@Component
public interface Event<T> {

    void execute(T event);

    Class getEvent();

}
