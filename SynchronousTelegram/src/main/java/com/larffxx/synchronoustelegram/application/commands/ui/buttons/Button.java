package com.larffxx.synchronoustelegram.application.commands.ui.buttons;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import org.springframework.stereotype.Component;

@Component
public interface Button<T> {
    T clicked(UpdateReceiver updateReceiver);

    String getButton();
}
