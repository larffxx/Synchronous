package com.larffxx.synchronoustelegram.service.registry;

import org.springframework.stereotype.Component;

@Component
public interface Registry<T> {
    T get(String t);
}
