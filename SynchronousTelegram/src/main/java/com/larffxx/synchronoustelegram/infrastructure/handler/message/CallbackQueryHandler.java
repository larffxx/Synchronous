package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.ButtonConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.infrastructure.ButtonContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaCommandProducer;
import com.larffxx.synchronoustelegram.service.controller.ui.buttons.Button;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientButtonExecutorService;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.registry.ButtonRegistry;
import com.larffxx.synchronoustelegram.service.utility.PendingOptionService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Handles Telegram callback query updates caused by inline button presses.
 * Resolves the button, asks for missing options when needed, executes the button, and forwards the command to Kafka.
 */
@Component
public class CallbackQueryHandler implements MessageHandler {
    /**
     * Service that executes button actions against the Telegram client.
     */
    private final TelegramClientButtonExecutorService buttonExecutor;
    /**
     * Producer that forwards button commands to Kafka.
     */
    private final TelegramKafkaCommandProducer telegramKafkaCommandProducer;
    /**
     * Resolver that builds a command context from a callback query.
     */
    private final ButtonContextResolver buttonContextResolver;
    /**
     * Registry that looks up buttons by name.
     */
    private final ButtonRegistry buttonRegistry;
    /**
     * Service that tracks buttons waiting for option input.
     */
    private final PendingOptionService pendingOptionService;
    /**
     * Service used to prompt the user for missing options.
     */
    private final TextMessageService textMessageService;

    /**
     * Creates the handler with its collaborators.
     * @param buttonExecutor executor for button actions
     * @param telegramKafkaCommandProducer producer for button commands
     * @param buttonContextResolver resolver for button contexts
     * @param buttonRegistry registry for button lookup
     * @param pendingOptionService service tracking pending options
     * @param textMessageService service for option prompts
     */
    public CallbackQueryHandler(TelegramClientButtonExecutorService buttonExecutor, TelegramKafkaCommandProducer telegramKafkaCommandProducer, ButtonContextResolver buttonContextResolver, ButtonRegistry buttonRegistry, PendingOptionService pendingOptionService, TextMessageService textMessageService) {
        this.buttonExecutor = buttonExecutor;
        this.telegramKafkaCommandProducer = telegramKafkaCommandProducer;
        this.buttonContextResolver = buttonContextResolver;
        this.buttonRegistry = buttonRegistry;
        this.pendingOptionService = pendingOptionService;
        this.textMessageService = textMessageService;
    }

    /**
     * Handles an incoming callback query update from a button press.
     * @param update the Telegram update holding the callback query
     * @throws InvalidCommandException if the pressed button is unknown
     */
    @Override
    public void handle(Update update) {
        CommandContext buttonContext = buttonContextResolver.resolveButtonContext(update);

        Button button = buttonRegistry.get(buttonContext.commandName());
        if (button == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }

        if (button.requiresOptions()) {
            buttonExecutor.answerCallback(update);
            pendingOptionService.expectOptions(buttonContext.chatId(), buttonContext.commandName());
            textMessageService.send(buttonContext.chatId(), ButtonConstant.optionPrompt(buttonContext.commandName()));
            return;
        }

        buttonExecutor.execute(update, buttonContext);
        telegramKafkaCommandProducer.sendKafkaMessage(buttonContext);
    }

    /**
     * Rejects callback handling for synced messages.
     * @param messageContext the synced message context
     * @throws TelegramException always, because buttons are not supported for synced messages
     */
    @Override
    public void handle(MessageContext messageContext) {
        throw new TelegramException("Callback buttons are not supported for synced messages");
    }

    /**
     * Returns the handler type for callback queries.
     * @return the callback query message type
     */
    @Override
    public String getType() {
        return String.valueOf(MessageType.CALLBACK_QUERY);
    }
}
