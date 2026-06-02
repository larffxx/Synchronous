package com.larffxx.synchronousdiscord.domain.exception.service;

import com.larffxx.synchronousdiscord.domain.exception.DiscordSynchronousException;

public class GuildNotFoundException extends DiscordSynchronousException {
    public GuildNotFoundException(String message) {
        super(message);
    }
}
