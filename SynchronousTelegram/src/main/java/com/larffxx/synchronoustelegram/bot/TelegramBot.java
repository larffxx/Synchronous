package com.larffxx.synchronoustelegram.bot;

import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.listeners.CommandListener;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
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
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;

@Component
@Getter
@Setter
@NoArgsConstructor
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    @Value("${token}")
    private String botToken;
    private CommandListener commandListener;
    private UpdateReceiver updateReceiver;
    private CommandPreProcessor commandPreProcessor;
    private TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    private TelegramClient telegramClient;

    @Autowired
    public TelegramBot(CommandListener commandListener, UpdateReceiver updateReceiver, CommandPreProcessor commandPreProcessor, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.commandListener = commandListener;
        this.updateReceiver = updateReceiver;
        this.commandPreProcessor = commandPreProcessor;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
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
        commandListener.saveToUpdateReceiver(update);
        if (!update.getMessage().getFrom().getIsBot()) {
            if (update.getMessage().hasText()) {
                if (update.getMessage().getText().startsWith("/")) {
                    String[] s = update.getMessage().getText().split(" ");
                    Command command = commandPreProcessor.getCommand(s[0]);
                    command.execute(updateReceiver);
                    telegramKafkaCommandProducer.sendKafkaMessage(update);
                } else {
                    telegramKafkaMessageProducer.sendKafkaMessage(update);
                }
            }
            if (update.getMessage().hasPhoto()) {
                try {
                    telegramKafkaMessageProducer.sendKafkaMessage(update, new File
                            (String.valueOf(telegramClient.downloadFile(telegramClient.execute(new GetFile(update.getMessage().getPhoto().get(2).getFileId()))))));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

