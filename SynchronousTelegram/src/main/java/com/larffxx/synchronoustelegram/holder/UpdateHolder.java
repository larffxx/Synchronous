package com.larffxx.synchronoustelegram.holder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UpdateHolder {
    private TelegramClient telegramClient;
    private Update update;
    private String chatId;
}

