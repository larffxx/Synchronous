package com.larffxx.synchronousdiscord.domain.exception.consume;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class ConsumingException extends DiscordSynchronousException {
    public ConsumingException(String message) {
        super(message);
    }
}
