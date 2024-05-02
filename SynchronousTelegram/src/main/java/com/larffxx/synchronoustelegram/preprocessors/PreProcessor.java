package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T> {
    T getCommand(String command);

    T getCommand(UpdateReceiver updateReceiver);
}
