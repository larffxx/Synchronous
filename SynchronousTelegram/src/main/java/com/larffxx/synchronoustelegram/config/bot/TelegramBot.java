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

@Component
@Getter
@Setter
@NoArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @Value("${token}")
    private String botToken;
    private TelegramClient telegramClient;
    private TelegramMessageConsumer telegramMessageConsumer;
    private UpdateReceiver updateReceiver;

    @Autowired
    public TelegramBot(TelegramMessageConsumer telegramMessageConsumer, UpdateReceiver updateReceiver) {
        this.telegramMessageConsumer = telegramMessageConsumer;
        this.updateReceiver = updateReceiver;
    }


    @PostConstruct
    public void initClient() {
        telegramClient = new OkHttpTelegramClient(botToken);
        updateReceiver.setTelegramClient(telegramClient);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }


    @Override
    public void consume(Update update) {
        telegramMessageConsumer.consumeMessage(update);
    }
}

