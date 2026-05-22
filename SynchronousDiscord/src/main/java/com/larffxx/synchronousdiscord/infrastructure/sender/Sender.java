package com.larffxx.synchronousdiscord.infrastructure.sender;


public interface Sender <T> {
    void send(T data);
    String getSender();
}
