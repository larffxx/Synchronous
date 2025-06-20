package com.larffxx.synchronoustelegram.domain.exception.execution;

public class SendingMessageToTelegramException extends ExecutionException {
    public SendingMessageToTelegramException(String message) {
        super(message);
    }
}
