package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class Command {
    private final UpdateReceiver updateReceiver;

    public Command(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    public abstract void execute(UpdateReceiver updateReceiver);
    public abstract String getCommand();
}
