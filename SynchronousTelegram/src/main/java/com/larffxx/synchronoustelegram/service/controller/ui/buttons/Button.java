package com.larffxx.synchronoustelegram.service.controller.ui.buttons;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import org.springframework.stereotype.Component;
//TODO:BUTTON INTERFACE
@Component
public interface Button<T> {
    T clicked(UpdateReceiver updateReceiver);

    String getButton();
}
