package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

public interface Sender {
    void send(MessageContext messageContext);
}
