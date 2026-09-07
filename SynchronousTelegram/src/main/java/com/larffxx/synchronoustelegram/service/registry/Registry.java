package com.larffxx.synchronoustelegram.service.registry;

import org.springframework.stereotype.Component;

/**
 * Generic contract for name based bean registries.
 *
 * @param <T> type of registered component
 */
@Component
public interface Registry<T> {
    /**
     * Returns the component registered under the given key.
     *
     * @param t registry key to look up
     * @return matching component or null if none is registered
     */
    T get(String t);
}
