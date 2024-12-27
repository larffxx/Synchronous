package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T> {
    T getCommand(String command);

    T getCommand(UpdateHolder updateHolder);
}
