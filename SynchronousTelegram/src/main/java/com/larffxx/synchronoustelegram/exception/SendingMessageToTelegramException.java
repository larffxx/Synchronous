package com.larffxx.synchronoustelegram.exception;

public class SendingMessageToTelegramException extends TelegramException{
    public SendingMessageToTelegramException(String message) {
        super(message);
    }
}
