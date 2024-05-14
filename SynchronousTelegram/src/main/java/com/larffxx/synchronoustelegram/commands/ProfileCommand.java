package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ProfileCommand extends Command{
    public ProfileCommand(UpdateReceiver updateReceiver) {
        super(updateReceiver);
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {

    }

    @Override
    public String getCommand() {
        return "/profile";
    }
}
