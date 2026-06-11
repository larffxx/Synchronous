package com.larffxx.synchronoustelegram.domain.context;

import java.util.List;

public record CommandContext(
        Long chatId,
        String commandAuthorName,
        String commandName,
        List<String> options
) {
}
