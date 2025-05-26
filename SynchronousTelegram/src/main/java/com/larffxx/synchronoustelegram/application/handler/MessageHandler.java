package com.larffxx.synchronoustelegram.application.handler;

import com.larffxx.synchronoustelegram.application.executor.TelegramClientCommandExecutor;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
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
    private final UpdateHandler updateHandler;
    private final PhotoHandler photoHandler;

    public MessageHandler(TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, TelegramClientCommandExecutor clientCommandExecutor, UpdateHandler updateHandler, PhotoHandler photoHandler) {
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.clientCommandExecutor = clientCommandExecutor;
        this.updateHandler = updateHandler;
        this.photoHandler = photoHandler;
    }

    public void handleMessage(Update update) {
        updateHandler.handleUpdate(update);

        if(update.getMessage().getFrom().getIsBot()) {
            return;
        }

        defineMessage(update);
    }

    private void defineMessage(Update update) {
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
