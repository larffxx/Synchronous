package com.larffxx.synchronousdiscord.domain.exception;

/**
 * Route Service Exception class.
 */
public class RouteServiceException extends DiscordSynchronousException {
    /**
     * Creates a new RouteServiceException.
     * @param message the message.
     */
    public RouteServiceException(String message) {
        super(message);
    }
}
