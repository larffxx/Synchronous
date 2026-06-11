package com.larffxx.synchronoustelegram.service.controller;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Command {
    void execute(CommandContext commandContext) throws TelegramApiException;
    String getCommand();
}
