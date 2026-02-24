package com.larffxx.synchronoustelegram.preprocessors;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import org.springframework.stereotype.Component;

@Component
public interface PreProcessor<T> {
    T get(String t);

    T get(UpdateReceiver updateReceiver);
}
