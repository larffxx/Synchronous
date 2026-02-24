package com.larffxx.synchronoustelegram.infrastructure.receiver;

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
public class UpdateReceiver {
    private TelegramClient telegramClient;
    private Update update;
    private String chatId;

    public void receiveUpdate(Update update) {
        this.update = update;
        this.chatId = String.valueOf(update.getMessage().getChatId());
    }
}
