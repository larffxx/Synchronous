package com.larffxx.synchronousdiscord.domain.exception.service;

public class GuildNotFoundException extends RuntimeException {
    public GuildNotFoundException(String message) {
        super(message);
    }
}
