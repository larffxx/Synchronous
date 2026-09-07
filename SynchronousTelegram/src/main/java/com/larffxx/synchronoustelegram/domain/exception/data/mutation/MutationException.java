package com.larffxx.synchronoustelegram.domain.exception.data.mutation;

import com.larffxx.synchronoustelegram.domain.exception.data.DataException;

/**
 * Base exception for failures while transforming downloaded data. Parent of photo conversion failures.
 */
public class MutationException extends DataException {
    public MutationException(String message) {
        /**
         * Creates the exception with a detail message.
         * @param message the detail message
         */
        super(message);
    }
}
