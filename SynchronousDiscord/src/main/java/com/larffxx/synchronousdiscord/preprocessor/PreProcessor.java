package com.larffxx.synchronousdiscord.preprocessor;

import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T>{
    T getCommand(String command);
}
