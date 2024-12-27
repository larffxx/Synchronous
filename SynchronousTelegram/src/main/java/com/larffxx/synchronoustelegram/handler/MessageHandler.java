package com.larffxx.synchronoustelegram.handler;

import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.exception.TelegramException;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaMessageProducer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.File;

@Getter
@Setter
@Component
public class MessageHandler {
    private final UpdateHolder updateHolder;
    private final CommandPreProcessor commandPreProcessor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public MessageHandler(UpdateHolder updateHolder, CommandPreProcessor commandPreProcessor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.updateHolder = updateHolder;
        this.commandPreProcessor = commandPreProcessor;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
    }

    public void handleMessage(Update update) {
        if(update.getMessage().hasPhoto()){
            photoMessageReceived(update);
        }else {
            textMessageReceived(update);
        }
    }

    private void textMessageReceived(Update update){
        if (update.getMessage().getText().startsWith("/")) {
            String[] s = update.getMessage().getText().split(" ");
            Command command = commandPreProcessor.getCommand(s[0]);

            try {
                command.execute(updateHolder);
            } catch (TelegramApiException e) {
                throw new TelegramException(e.getMessage());
            }
            telegramKafkaCommandProducer.sendKafkaMessage(update);
        } else {
            telegramKafkaMessageProducer.sendKafkaMessage(update);
        }
    }

    private void photoMessageReceived(Update update) {
        TelegramClient telegramClient = updateHolder.getTelegramClient();
        try {
            telegramKafkaMessageProducer.sendKafkaMessage(update, new File
                    (String.valueOf(telegramClient.downloadFile(telegramClient.execute(new GetFile(update.getMessage().getPhoto().get(2).getFileId()))))));
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
