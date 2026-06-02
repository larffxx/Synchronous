package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class NoConnectionBetweenServersException extends DiscordSynchronousException {
    public NoConnectionBetweenServersException(String message) {
        super(message);
    }
}
