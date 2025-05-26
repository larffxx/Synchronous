package com.larffxx.synchronoustelegram.application.commands.ui.buttons;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import org.springframework.stereotype.Component;

@Component
public interface Button<T> {
    T clicked(UpdateHandler updateHandler);

    String getButton();
}
