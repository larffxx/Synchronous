package com.larffxx.synchronoustelegram.domain.exception.data;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

public class DataException extends TelegramException {
    public DataException(String message) {
        super(message);
    }
}
