package com.larffxx.synchronoustelegram.infrastructure.consumer;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import com.larffxx.synchronoustelegram.service.utility.MessageDefineService;
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
     * Receiver that stores the latest update.
     */
    private final UpdateReceiver updateReceiver;
    /**
     * Service that classifies the type of an incoming update.
     */
    private final MessageDefineService messageDefineService;
    /**
     * Service that dispatches updates to their handlers.
     */
    private final MessageDispatcherService messageDispatcherService;

    /**
     * Creates the consumer with its collaborators.
     * @param messageDefineService service classifying update types
     * @param updateReceiver receiver storing updates
     * @param messageDispatcherService service dispatching updates
     */
    public TelegramMessageConsumer(MessageDefineService messageDefineService, UpdateReceiver updateReceiver, MessageDispatcherService messageDispatcherService) {
        this.messageDefineService = messageDefineService;
        this.updateReceiver = updateReceiver;
        this.messageDispatcherService = messageDispatcherService;
    }

    /**
     * Consumes one Telegram update and dispatches it for handling.
     * @param update the Telegram update to consume
     */
    public void consumeMessage(Update update) {
        updateReceiver.receiveUpdate(update);

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