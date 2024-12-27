package com.larffxx.synchronousdiscord.addition;

import org.springframework.stereotype.Component;

@Component
public interface Addable <T> {
    void add(T t);
}
