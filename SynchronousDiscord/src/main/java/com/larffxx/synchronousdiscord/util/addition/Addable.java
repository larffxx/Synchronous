package com.larffxx.synchronousdiscord.util.addition;

import org.springframework.stereotype.Component;

@Component
public interface Addable <T> {
    void add(T t);
}
