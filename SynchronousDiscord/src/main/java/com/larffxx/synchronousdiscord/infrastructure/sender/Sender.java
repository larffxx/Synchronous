package com.larffxx.synchronousdiscord.infrastructure.sender;

import org.springframework.stereotype.Component;

@Component
public interface Sender <T> {
    void send(T data);
    String getSender();
}
