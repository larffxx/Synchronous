package com.larffxx.synchronoustelegram.infrastructure.receiver;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.objects.Update;

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
     * The most recently received Telegram update.
     */
    private Update update;
    /**
     * Identifier of the chat related to the current interaction.
     */
    private String chatId;
    /**
     * Option value stored for the current interaction.
     */
    private String option;
    /**
     * Telegram client used to execute Bot API calls.
     */
    private TelegramClient telegramClient;

    /**
     * Stores the latest incoming update.
     * @param update the Telegram update to store
     */
    public void receiveUpdate(Update update) {
        this.update = update;
    }
}
