package com.larffxx.synchronousdiscord.service.registry;


/**
 * Lookup contract for named handlers such as commands and senders.
 */
public interface Registry<T>{
    /**
     * Returns the handler registered under the given name.
     *
     * @param command registered handler name
     * @return handler for the given name
     */
    T getSender(String command);
}
