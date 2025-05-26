package com.larffxx.synchronoustelegram.bot;

import com.larffxx.synchronoustelegram.application.handler.MessageHandler;
import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
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
    private MessageHandler messageHandler;
    private UpdateHandler updateHandler;

    @Autowired
    public TelegramBot(MessageHandler messageHandler, UpdateHandler updateHandler) {
        this.messageHandler = messageHandler;
        this.updateHandler = updateHandler;
    }


    @PostConstruct
    public void initClient() {
        telegramClient = new OkHttpTelegramClient(botToken);
        updateHandler.setTelegramClient(telegramClient);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }


    @Override
    public void consume(Update update) {
        messageHandler.handleMessage(update);
    }
}

