package com.larffxx.synchronoustelegram.buttons;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;

@Component
public interface Button<T> {
    T clicked(UpdateReceiver updateReceiver);

    String getButton();
}
