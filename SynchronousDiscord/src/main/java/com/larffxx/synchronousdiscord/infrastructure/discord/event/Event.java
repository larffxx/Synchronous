package com.larffxx.synchronousdiscord.infrastructure.discord.event;


public interface Event<T> {

    void execute(T event);

    Class getEvent();

}
