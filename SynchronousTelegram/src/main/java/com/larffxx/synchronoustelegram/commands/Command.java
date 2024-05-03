package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;

@Component
public interface Command {
    void execute(UpdateReceiver updateReceiver);

    String getCommand();
}
