package com.larffxx.synchronoustelegram.domain.record;

/**
 * Immutable error response returned by the REST exception handler.
 * @param infExc short error category label
 * @param msg detailed error message
 */
public record ErrorResponse(String infExc, String msg) {
}
