package com.larffxx.synchronousdiscord.domain.exception.consume;

/**
 * Message Consuming Exception class.
 */
public class MessageConsumingException extends ConsumingException {
    /**
     * Creates a new MessageConsumingException.
     * @param message the message.
     */
    public MessageConsumingException(String message) {
        super(message);
    }
}
