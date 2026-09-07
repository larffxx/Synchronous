package com.larffxx.synchronousdiscord.domain.context;

/**
 * Error Context record.
 * @param code the code.
 * @param message the message.
 */
public record ErrorContext(
    String code,
    String message
) {}
