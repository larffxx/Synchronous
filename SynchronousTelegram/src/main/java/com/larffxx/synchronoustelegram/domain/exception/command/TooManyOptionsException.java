package com.larffxx.synchronoustelegram.domain.exception.command;

public class TooManyOptionsException extends CommandException {
    public TooManyOptionsException(String message) {
        super(message);
    }
}
