package com.larffxx.synchronoustelegram.application.handler;

import com.larffxx.synchronoustelegram.application.executor.TelegramClientCommandExecutor;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.util.constant.MessageType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextMessageHandler implements MessageHandler {
    private final TelegramClientCommandExecutor clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public TextMessageHandler(TelegramClientCommandExecutor clientCommandExecutor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.clientCommandExecutor = clientCommandExecutor;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
    }

    @Override
    public void handle(Update update) {
        if (update.getMessage().getText().startsWith("/")) {
            clientCommandExecutor.execute(update);

            telegramKafkaCommandProducer.sendKafkaMessage(update);
        } else {
            telegramKafkaMessageProducer.sendKafkaMessage(update);
        }
    }
    @Override
    public String getType() {
        return MessageType.textMessage;
    }
}
