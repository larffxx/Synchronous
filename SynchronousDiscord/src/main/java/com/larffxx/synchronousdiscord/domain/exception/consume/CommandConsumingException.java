package com.larffxx.synchronousdiscord.domain.exception.consume;

/**
 * Command Consuming Exception class.
 */
public class CommandConsumingException extends ConsumingException {
    /**
     * Creates a new CommandConsumingException.
     * @param message the message.
     */
    public CommandConsumingException(String message) {
        super(message);
    }
}
