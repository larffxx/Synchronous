package com.larffxx.synchronoustelegram.domain.exception.command;

import com.larffxx.synchronoustelegram.domain.exception.TelegramException;

/**
 * Base exception for command parsing and handling failures. Parent of connect, register, and validation failures.
 */
public class CommandException extends TelegramException {
    public CommandException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
