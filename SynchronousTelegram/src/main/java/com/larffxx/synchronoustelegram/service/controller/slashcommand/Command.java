package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Contract for Telegram slash commands.
 */
public interface Command {
    /**
     * Executes the command with the given context.
     *
     * @param commandContext parsed command invocation context
     * @throws TelegramApiException if the Telegram API call fails
     */
    void execute(CommandContext commandContext) throws TelegramApiException;
    /**
     * Returns the command name.
     *
     * @return command name without leading slash
     */
    String getCommand();
}
