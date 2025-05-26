package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T> {
    T getCommand(String command);

    T getCommand(UpdateHandler updateHandler);
}
