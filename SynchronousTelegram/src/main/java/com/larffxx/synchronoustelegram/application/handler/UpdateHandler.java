package com.larffxx.synchronoustelegram.application.handler;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@NoArgsConstructor
public class UpdateHandler {
    private TelegramClient telegramClient;
    private Update update;
    private String chatId;

    public void handleUpdate(Update update) {
        this.update = update;
        this.chatId = String.valueOf(update.getMessage().getChatId());
    }
}
