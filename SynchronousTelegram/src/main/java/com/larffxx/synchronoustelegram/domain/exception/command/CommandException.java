package com.larffxx.synchronoustelegram.domain.exception.command;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

public class CommandException extends TelegramException {
    public CommandException(String message) {
        super(message);
    }
}
