package com.larffxx.synchronoustelegram.config.bot;

import com.larffxx.synchronoustelegram.infrastructure.consumer.TelegramMessageConsumer;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * Spring long polling bot that connects the Telegram API to the application.
 * Delegates every incoming update to the Telegram message consumer.
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    /**
     * Bot token injected from application properties.
     */
    @Value("${token}")
    private String botToken;
    /**
     * Telegram client used to execute Bot API calls.
     */
    private TelegramClient telegramClient;
    /**
     * Consumer that processes incoming Telegram updates.
     */
    private TelegramMessageConsumer telegramMessageConsumer;
    /**
     * Receiver that stores the latest update and exposes the Telegram client.
     */
    private UpdateReceiver updateReceiver;

    /**
     * Creates the bot with its update collaborators.
     * @param telegramMessageConsumer consumer for incoming updates
     * @param updateReceiver receiver shared with senders
     */
    @Autowired
    public TelegramBot(TelegramMessageConsumer telegramMessageConsumer, UpdateReceiver updateReceiver) {
        this.telegramMessageConsumer = telegramMessageConsumer;
        this.updateReceiver = updateReceiver;
    }


    /**
     * Initializes the Telegram client after dependency injection and registers it with the update receiver.
     */
    @PostConstruct
    public void initClient() {
        telegramClient = new OkHttpTelegramClient(botToken);
        updateReceiver.setTelegramClient(telegramClient);
    }

    /**
     * Returns this bot as the long polling update consumer.
     * @return this instance as the update consumer
     */
    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }


    /**
     * Passes an incoming update to the message consumer.
     * @param update the Telegram update to process
     */
    @Override
    public void consume(Update update) {
        telegramMessageConsumer.consumeMessage(update);
    }
}

