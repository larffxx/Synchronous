package com.larffxx.synchronoustelegram.domain.context;

import java.util.List;

/**
 * Immutable context describing an executed command.
 * Carries the chat, the executor, the command name, its options, and collected responses.
 * @param chatId identifier of the Telegram chat where the command was issued
 * @param executedBy name of the user who executed the command
 * @param commandName name of the executed command
 * @param options options supplied with the command
 * @param response responses collected during execution
 */
public record CommandContext(
        Long chatId,
        String executedBy,
        String commandName,
        List<String> options,
        List<String> response
) {
}
