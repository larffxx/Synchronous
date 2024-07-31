package com.larffxx.synchronousdiscord.senders;

import org.springframework.stereotype.Component;

@Component
public interface Sender <T> {
    void send(T data);
    String getSender();
}
