package com.larffxx.synchronoustelegram.buttons;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;

@Component
public interface Button<T> {
    T clicked(UpdateHolder updateHolder);

    String getButton();
}
