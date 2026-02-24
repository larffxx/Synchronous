package com.larffxx.synchronoustelegram.application.handler;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void handle(Update update);
    String getType();
}
