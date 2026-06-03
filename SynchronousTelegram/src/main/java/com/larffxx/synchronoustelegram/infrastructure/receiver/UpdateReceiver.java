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
    private Update update;
    private String chatId;
    private String option;
    private TelegramClient telegramClient;

    public void receiveUpdate(Update update) {
        this.update = update;
    }
}
