package com.larffxx.synchronousdiscord.infrastructure.discord.event;


/**
 * Contract for a Discord gateway event handler.
 */
public interface Event<T> {

    /**
     * Handles the given Discord event.
     *
     * @param event Discord event to handle
     */
    void execute(T event);

    /**
     * Returns the Discord event class handled by this handler.
     *
     * @return handled Discord event class
     */
    Class getEvent();

}
