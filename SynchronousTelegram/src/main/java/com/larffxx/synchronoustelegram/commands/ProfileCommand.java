package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public class ProfileCommand extends Command{
    public ProfileCommand(UpdateHolder updateHolder) {
        super(updateHolder);
    }

    @Override
    public void execute(UpdateHolder updateHolder) throws TelegramApiException {

    }

    @Override
    public String getCommand() {
        return "/profile";
    }
}
