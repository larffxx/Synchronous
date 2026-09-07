package com.larffxx.synchronoustelegram.infrastructure.consumer;

import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Entry point for incoming Telegram updates.
 * Stores each update, ignores bot senders and empty updates, and dispatches the rest for handling.
 */
@Getter
@Setter
@Component
public class TelegramMessageConsumer {
    /**
     * Service that dispatches updates to their handlers.
     */
    private final MessageDispatcherService messageDispatcherService;

    /**
     * Creates the consumer with its collaborators.
     * @param messageDispatcherService service dispatching updates
     */
    public TelegramMessageConsumer(MessageDispatcherService messageDispatcherService) {
        this.messageDispatcherService = messageDispatcherService;
    }

    /**
     * Consumes one Telegram update and dispatches it for handling.
     * @param update the Telegram update to consume
     */
    public void consumeMessage(Update update) {
        if (update.hasCallbackQuery()) {
            messageDispatcherService.dispatch(update);
            return;
        }

        if (!update.hasMessage() || update.getMessage().getFrom().getIsBot()) {
            return;
        }

        messageDispatcherService.dispatch(update);
    }
}