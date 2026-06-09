package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.CommandContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.service.registry.MessageServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TextMessageHandler implements MessageHandler {
    private final CommandContextResolver commandContextResolver;
    private final MessageServiceRegistry messageServiceRegistry;
    private final TelegramClientCommandExecutorService clientCommandExecutor;
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;

    public TextMessageHandler(MessageServiceRegistry messageServiceRegistry, TelegramClientCommandExecutorService clientCommandExecutor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, CommandContextResolver commandContextResolver) {
        this.messageServiceRegistry = messageServiceRegistry;
        this.clientCommandExecutor = clientCommandExecutor;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.commandContextResolver = commandContextResolver;
    }

    @Override
    public void handle(Update update) {
        CommandContext commandContext = commandContextResolver.resolveCommandContext(update);

        if (update.getMessage().getText().startsWith("/")) {
            clientCommandExecutor.execute(update);

            telegramKafkaCommandProducer.sendKafkaMessage(update);
        } else {
            telegramKafkaMessageProducer.sendKafkaMessage(update);
        }
    }

    @Override
    public void handle(DiscordPayload discordPayload) {
        messageServiceRegistry.get(String.valueOf(MessageType.TEXT_MESSAGE)).send(discordPayload);
    }

    @Override
    public String getType() {
        return String.valueOf(MessageType.TEXT_MESSAGE);
    }
}
