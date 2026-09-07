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

    /**
     * Creates a new CommandConsumingException with a cause.
     * @param message the message.
     * @param cause the cause.
     */
    public CommandConsumingException(String message, Throwable cause) {
        super(message, cause);
    }
}
