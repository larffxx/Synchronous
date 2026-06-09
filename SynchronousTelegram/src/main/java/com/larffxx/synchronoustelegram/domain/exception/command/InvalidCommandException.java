package com.larffxx.synchronoustelegram.domain.exception.command;

public class InvalidCommandException extends CommandException {
    public InvalidCommandException(String message) {
        super(message);
    }
}
