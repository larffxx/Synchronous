package com.larffxx.synchronousdiscord.domain.exception.service;

public class TextChannelNotFoundException extends RuntimeException {
    public TextChannelNotFoundException(String message) {
        super(message);
    }
}
