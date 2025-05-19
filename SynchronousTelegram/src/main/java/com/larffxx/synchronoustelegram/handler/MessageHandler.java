package com.larffxx.synchronoustelegram.handler;

import com.larffxx.synchronoustelegram.exception.ReceivingPhotoException;
import com.larffxx.synchronoustelegram.executor.TelegramClientCommandExecutor;
import com.larffxx.synchronoustelegram.handler.utility.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.infexc.InfExcMessage;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaMessageProducer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import org.telegram.telegrambots.meta.api.objects.File;

@Getter
@Setter
@Component
public class MessageHandler {
    private final UpdateHolder updateHolder;
    private final TmpToJpgConverter tmpToJpgConverter;
    private final TelegramClientCommandExecutor clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public MessageHandler(UpdateHolder updateHolder, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, TelegramClientCommandExecutor clientCommandExecutor, TmpToJpgConverter tmpToJpgConverter) {
        this.updateHolder = updateHolder;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.clientCommandExecutor = clientCommandExecutor;
        this.tmpToJpgConverter = tmpToJpgConverter;
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

        Message message = update.getMessage();
        String getFileId = message.getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
        try {
            File file = telegramClient.execute(new GetFile(getFileId));

            java.io.File downloadedFile = telegramClient.downloadFile(file);

            telegramKafkaMessageProducer.sendKafkaMessage(update, tmpToJpgConverter.tmpConvertToJpg(downloadedFile, Long.valueOf(message.getMessageId())));
        } catch (TelegramApiException e) {
            throw new ReceivingPhotoException(InfExcMessage.RECEIVING_PHOTO_EXCEPTION);
        }
    }
}
