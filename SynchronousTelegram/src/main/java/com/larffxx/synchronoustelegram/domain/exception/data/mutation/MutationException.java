package com.larffxx.synchronoustelegram.domain.exception.data.mutation;

import com.larffxx.synchronoustelegram.domain.exception.data.DataException;

public class MutationException extends DataException {
    public MutationException(String message) {
        super(message);
    }
}
