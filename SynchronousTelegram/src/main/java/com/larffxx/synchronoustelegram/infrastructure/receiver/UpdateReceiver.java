package com.larffxx.synchronoustelegram.infrastructure.receiver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Holds the latest Telegram update and shared sending state.
 * Exposes the Telegram client to senders after bot startup.
 */
@Getter
@Setter
@Component
@NoArgsConstructor
public class UpdateReceiver {
    /**
     * Telegram client used to execute Bot API calls.
     */
    private TelegramClient telegramClient;
}
