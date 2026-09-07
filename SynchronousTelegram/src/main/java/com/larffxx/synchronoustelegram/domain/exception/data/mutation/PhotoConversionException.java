package com.larffxx.synchronoustelegram.domain.exception.data.mutation;

/**
 * Signals that a downloaded photo could not be converted.
 */
public class PhotoConversionException extends MutationException {
    public PhotoConversionException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
