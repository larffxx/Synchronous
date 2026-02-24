package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public abstract class Command {
    private final UpdateReceiver updateReceiver;

    public Command(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    public abstract void execute(UpdateReceiver update) throws TelegramApiException;
    public abstract void execute(DiscordPayload discordPayload) throws TelegramApiException;
    public abstract String getCommand();
}
