package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import lombok.Getter;
import lombok.Setter;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public abstract class Command {
    private final UpdateHandler updateHandler;

    public Command(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    public abstract void execute(UpdateHandler update) throws TelegramApiException;
    public abstract void execute(DiscordPayload discordPayload) throws TelegramApiException;
    public abstract String getCommand();
}
