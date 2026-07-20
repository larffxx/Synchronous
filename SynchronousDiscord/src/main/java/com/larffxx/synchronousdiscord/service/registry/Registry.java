package com.larffxx.synchronousdiscord.service.registry;


public interface Registry<T>{
    T getSender(String command);
}
