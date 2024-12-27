package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public abstract class Command {
    private final UpdateHolder updateHolder;

    public Command(UpdateHolder updateHolder) {
        this.updateHolder = updateHolder;
    }

    public abstract void execute(UpdateHolder updateHolder) throws TelegramApiException;
    public abstract String getCommand();
}
