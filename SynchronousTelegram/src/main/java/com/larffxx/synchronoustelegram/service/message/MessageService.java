package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;

public interface MessageService {
    void send(MessageContext messageContext);

    String getType();
}
