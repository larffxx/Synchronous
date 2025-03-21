package com.larffxx.synchronoustelegram.handler;

import com.larffxx.synchronoustelegram.exception.ReceivingPhotoException;
import com.larffxx.synchronoustelegram.executor.TelegramClientCommandExecutor;
import com.larffxx.synchronoustelegram.infexc.InfExcMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
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
    private final TelegramClientCommandExecutor clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public MessageHandler(UpdateHolder updateHolder, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, TelegramClientCommandExecutor clientCommandExecutor) {
        this.updateHolder = updateHolder;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.clientCommandExecutor = clientCommandExecutor;
    }

    public void handleMessage(Update update) {
        if (update.getMessage().hasPhoto()) {
            photoMessageReceived(update);
        } else {
            textMessageReceived(update);
        }
    }

    private void textMessageReceived(Update update) {
        if (update.getMessage().getText().startsWith("/")) {
            clientCommandExecutor.execute(update);

            telegramKafkaCommandProducer.sendKafkaMessage(update);
        } else {
            telegramKafkaMessageProducer.sendKafkaMessage(update);
        }
    }

    private void photoMessageReceived(Update update) {
        TelegramClient telegramClient = updateHolder.getTelegramClient();

        String getFileId = update.getMessage().getPhoto().get(2).getFileId();
        try {
            File file = new File(String.valueOf(telegramClient.downloadFile(getFileId)));

            telegramKafkaMessageProducer.sendKafkaMessage(update, file);
        } catch (TelegramApiException e) {
            throw new ReceivingPhotoException(InfExcMessage.RECEIVING_PHOTO_EXCEPTION);
        }
    }
}
