package com.larffxx.synchronoustelegram.domain.exception;

public class StringCommandExecutingException extends TelegramException {
    public StringCommandExecutingException(String message) {
        super(message);
    }
}
