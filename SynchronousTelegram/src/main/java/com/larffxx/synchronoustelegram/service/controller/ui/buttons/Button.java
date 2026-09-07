package com.larffxx.synchronoustelegram.service.controller.ui.buttons;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Contract for inline keyboard buttons.
 */
public interface Button {
    /**
     * Executes the button action with the given context.
     *
     * @param buttonContext parsed button invocation context
     * @throws TelegramApiException if the Telegram API call fails
     */
    void execute(CommandContext buttonContext) throws TelegramApiException;

    /**
     * Returns the button identifier.
     *
     * @return button identifier matching the related command name
     */
    String getButton();

    /**
     * Reports whether the button requires extra user provided options.
     *
     * @return true if options must be collected before execution
     */
    default boolean requiresOptions() {
        return false;
    }
}
