package com.larffxx.synchronoustelegram.domain.exception.execution;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

public class ExecutionException extends TelegramException {
    public ExecutionException(String message) {
        super(message);
    }
}
