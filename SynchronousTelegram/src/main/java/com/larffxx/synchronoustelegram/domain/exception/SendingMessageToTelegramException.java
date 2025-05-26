package com.larffxx.synchronoustelegram.domain.exception;

public class SendingMessageToTelegramException extends TelegramException{
    public SendingMessageToTelegramException(String message) {
        super(message);
    }
}
