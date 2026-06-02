package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class TextChannelNotFoundException extends DiscordSynchronousException {
    public TextChannelNotFoundException(String message) {
        super(message);
    }
}
