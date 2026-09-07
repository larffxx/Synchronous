package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.service.registry.MessageServiceRegistry;
import com.larffxx.synchronoustelegram.service.utility.PendingOptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles plain text updates, slash commands, and pending button option input.
 * Executes commands locally, forwards commands and messages to Kafka, and delivers synced text messages.
 */
@Component
public class TextMessageHandler implements MessageHandler {
    /**
     * Registry that looks up message services by type.
     */
    private final MessageServiceRegistry messageServiceRegistry;
    /**
     * Executor that runs slash commands against the Telegram client.
     */
    private final TelegramClientCommandExecutorService clientCommandExecutor;
    /**
     * Producer that forwards command payloads to Kafka.
     */
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    /**
     * Producer that forwards text payloads to Kafka.
     */
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    /**
     * Service that tracks buttons waiting for option input.
     */
    private final PendingOptionService pendingOptionService;

    /**
     * Creates the handler with its collaborators.
     * @param messageServiceRegistry registry for message services
     * @param clientCommandExecutor executor for slash commands
     * @param telegramKafkaCommandProducer producer for command payloads
     * @param telegramKafkaMessageProducer producer for text payloads
     * @param pendingOptionService service tracking pending options
     */
    public TextMessageHandler(MessageServiceRegistry messageServiceRegistry, TelegramClientCommandExecutorService clientCommandExecutor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, TelegramKafkaMessageProducer telegramKafkaMessageProducer, PendingOptionService pendingOptionService) {
        this.messageServiceRegistry = messageServiceRegistry;
        this.clientCommandExecutor = clientCommandExecutor;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
        this.pendingOptionService = pendingOptionService;
    }

    /**
     * Routes a text update to command execution, pending option handling, or Kafka publishing.
     * @param update the Telegram update carrying the text
     */
    @Override
    public void handle(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        if (text.startsWith("/")) {
            pendingOptionService.cancel(chatId);
            clientCommandExecutor.execute(update);

            telegramKafkaCommandProducer.sendKafkaMessage(update);
        } else {
            String pendingCommand = pendingOptionService.take(chatId);
            if (pendingCommand != null) {
                CommandContext buttonContext = new CommandContext(
                        chatId,
                        update.getMessage().getFrom().getUserName(),
                        pendingCommand,
                        List.of(text),
                        new ArrayList<>()
                );
                clientCommandExecutor.execute(buttonContext);

                telegramKafkaCommandProducer.sendKafkaMessage(buttonContext);
            } else {
                telegramKafkaMessageProducer.sendKafkaMessage(update);
            }
        }
    }

    /**
     * Sends a synced text message through the registered text message service.
     * @param messageContext the synced message context
     */
    @Override
    public void handle(MessageContext messageContext) {
        messageServiceRegistry.get(String.valueOf(MessageType.TEXT_MESSAGE)).send(messageContext);
    }

    /**
     * Returns the handler type for text messages.
     * @return the text message type
     */
    @Override
    public String getType() {
        return String.valueOf(MessageType.TEXT_MESSAGE);
    }
}
