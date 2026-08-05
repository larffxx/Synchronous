package com.larffxx.synchronoustelegram.domain.context;

import java.util.List;

public record CommandContext(
        Long chatId,
        String executedBy,
        String commandName,
        List<String> options,
        List<String> response
) {
}
