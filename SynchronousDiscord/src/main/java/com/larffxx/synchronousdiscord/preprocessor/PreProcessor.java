package com.larffxx.synchronousdiscord.preprocessor;

import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T>{
    T getCommand(String command);
}
