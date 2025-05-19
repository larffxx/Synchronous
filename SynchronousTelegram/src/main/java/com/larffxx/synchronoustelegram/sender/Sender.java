package com.larffxx.synchronoustelegram.sender;

public interface Sender {
    void send(Long chatId, String text);
}
