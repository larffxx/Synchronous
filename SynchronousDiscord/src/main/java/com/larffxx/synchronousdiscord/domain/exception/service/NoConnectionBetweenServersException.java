package com.larffxx.synchronousdiscord.domain.exception.service;

public class NoConnectionBetweenServersException extends RuntimeException {
    public NoConnectionBetweenServersException(String message) {
        super(message);
    }
}
