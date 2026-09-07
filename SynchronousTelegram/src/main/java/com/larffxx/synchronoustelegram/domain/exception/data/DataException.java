package com.larffxx.synchronoustelegram.domain.exception.data;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

/**
 * Base exception for data loading and conversion failures. Parent of input and mutation failures.
 */
public class DataException extends TelegramException {
    public DataException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
