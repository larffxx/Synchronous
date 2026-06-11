package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.service.registry.MessageServiceRegistry;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextMessageHandler implements MessageHandler {
    private final MessageServiceRegistry messageServiceRegistry;
    private final TelegramClientCommandExecutorService clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public TextMessageHandler(MessageServiceRegistry messageServiceRegistry, TelegramClientCommandExecutorService clientCommandExecutor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.messageServiceRegistry = messageServiceRegistry;
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
    public void handle(MessageContext messageContext) {
        messageServiceRegistry.get(String.valueOf(MessageType.TEXT_MESSAGE)).send(messageContext);
    }

    @Override
    public String getType() {
        return String.valueOf(MessageType.TEXT_MESSAGE);
    }
}
