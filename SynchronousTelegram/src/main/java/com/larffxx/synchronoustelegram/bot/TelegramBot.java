package com.larffxx.synchronoustelegram.bot;

import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.listeners.CommandListener;
import com.larffxx.synchronoustelegram.listeners.KafkaListeners;
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
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;
import java.util.Comparator;

@Component
@Getter
@Setter
@NoArgsConstructor
public class TelegramBot implements LongPollingSingleThreadUpdateConsumer {
    @Value("${name}")
    private String botUsername;
    @Value("${token}")
    private String botToken;
    private CommandListener commandListener;
    private UpdateReceiver updateReceiver;
    private CommandPreProcessor commandPreProcessor;
    private KafkaListeners kafkaListener;
    private TelegramClient telegramClient;

    @Autowired
    public TelegramBot(CommandListener commandListener, UpdateReceiver updateReceiver, CommandPreProcessor commandPreProcessor, KafkaListeners kafkaListener) {
        this.commandListener = commandListener;
        this.updateReceiver = updateReceiver;
        this.commandPreProcessor = commandPreProcessor;
        this.kafkaListener = kafkaListener;
    }

    @PostConstruct
    public void initClient() {
        telegramClient = new OkHttpTelegramClient(botToken);
    }

    @Override
    public void consume(Update update) {
        updateReceiver.setTelegramClient(telegramClient);
        commandListener.saveToUpdateReceiver(update);
        Command command;
        if (update.getMessage().hasText() && !update.getMessage().getFrom().getIsBot()) {
            if (update.getMessage().getText().startsWith("/")) {
                String[] s = update.getMessage().getText().split(" ");
                command = commandPreProcessor.getCommand(s[0]);
                if (command != null) {
                    command.execute(updateReceiver);
                } else {
                    kafkaListener.sendKafkaMessage(update, s[0]);
                }
            } else {
                kafkaListener.sendKafkaMessage(update);
            }
        }
        if (update.getMessage().hasPhoto() && !update.getMessage().getFrom().getIsBot()) {
            try {
                kafkaListener.sendKafkaMessage(update, new File(String.valueOf(telegramClient.downloadFile(telegramClient.execute(new GetFile(update.getMessage().getPhoto().get(2).getFileId()))))));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

