package com.larffxx.synchronoustelegram.domain.exception;

public class TelegramException extends RuntimeException {
    public TelegramException(String message) {
        super(message);
    }
}
