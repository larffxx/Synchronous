package com.larffxx.synchronousdiscord.event;


public interface Event<T> {

    void execute(T event);

    Class getEvent();

}
