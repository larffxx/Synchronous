package com.larffxx.synchronoustelegram.bot;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import com.larffxx.synchronoustelegram.receivers.MessageReceiver;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
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
    private UpdateHolder updateHolder;
    private MessageReceiver messageReceiver;

    @Autowired
    public TelegramBot(MessageReceiver messageReceiver, UpdateHolder updateHolder) {
        this.messageReceiver = messageReceiver;
        this.updateHolder = updateHolder;
    }


    @PostConstruct
    public void initClient() {
        telegramClient = new OkHttpTelegramClient(botToken);
        updateHolder.setTelegramClient(telegramClient);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }


    @Override
    public void consume(Update update) {
        messageReceiver.receiveMessage(update);
    }
}

