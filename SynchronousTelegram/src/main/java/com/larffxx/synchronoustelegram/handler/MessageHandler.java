package com.larffxx.synchronoustelegram.handler;

import com.larffxx.synchronoustelegram.executor.TelegramClientCommandExecutor;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.producer.TelegramKafkaMessageProducer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MessageHandler {
    private final TelegramClientCommandExecutor clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    private final PhotoHandler photoHandler;

    public MessageHandler(TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, TelegramClientCommandExecutor clientCommandExecutor, PhotoHandler photoHandler) {
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.clientCommandExecutor = clientCommandExecutor;
        this.photoHandler = photoHandler;
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
        telegramKafkaMessageProducer.sendKafkaMessage(photoHandler.handlePhotoMessage(update));
    }
}
