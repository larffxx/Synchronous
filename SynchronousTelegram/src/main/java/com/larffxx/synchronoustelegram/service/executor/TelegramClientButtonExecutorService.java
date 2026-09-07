package com.larffxx.synchronoustelegram.service.executor;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.infrastructure.ButtonContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.service.controller.ui.buttons.Button;
import com.larffxx.synchronoustelegram.service.registry.ButtonRegistry;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Executes inline button presses coming from the Telegram client.
 */
@Service
public class TelegramClientButtonExecutorService {
    /**
     * Registry of available inline buttons.
     */
    private final ButtonRegistry buttonRegistry;
    /**
     * Resolver that builds a button context from a raw update.
     */
    private final ButtonContextResolver buttonContextResolver;
    /**
     * Receiver that provides access to the Telegram client.
     */
    private final UpdateReceiver updateReceiver;

    /**
     * Creates a button executor with its dependencies.
     *
     * @param buttonRegistry registry of available buttons
     * @param buttonContextResolver resolver for button contexts
     * @param updateReceiver receiver providing the Telegram client
     */
    public TelegramClientButtonExecutorService(ButtonRegistry buttonRegistry, ButtonContextResolver buttonContextResolver, UpdateReceiver updateReceiver) {
        this.buttonRegistry = buttonRegistry;
        this.buttonContextResolver = buttonContextResolver;
        this.updateReceiver = updateReceiver;
    }

    /**
     * Resolves the button context from the update and executes it.
     *
     * @param update raw Telegram update carrying the callback query
     * @return resolved button context that was executed
     */
    public CommandContext execute(Update update) {
        return execute(update, buttonContextResolver.resolveButtonContext(update));
    }

    /**
     * Executes an already resolved button context.
     *
     * @param update raw Telegram update carrying the callback query
     * @param buttonContext resolved button context to execute
     * @return executed button context
     * @throws InvalidCommandException if no button matches the context
     * @throws TelegramException if the Telegram API call fails
     */
    public CommandContext execute(Update update, CommandContext buttonContext) {
        Button button = buttonRegistry.get(buttonContext.commandName());
        if (button == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }

        answerCallback(update);
        try {
            button.execute(buttonContext);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
        return buttonContext;
    }

    /**
     * Answers the callback query so the client stops showing progress.
     *
     * @param update raw Telegram update carrying the callback query
     * @throws TelegramException if the Telegram API call fails
     */
    public void answerCallback(Update update) {
        try {
            updateReceiver.getTelegramClient().execute(
                    AnswerCallbackQuery.builder()
                            .callbackQueryId(update.getCallbackQuery().getId())
                            .build()
            );
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
