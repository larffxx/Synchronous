package com.larffxx.synchronoustelegram.infrastructure.sender;

public interface Sender {
    void send(Long chatId, String text);
}
