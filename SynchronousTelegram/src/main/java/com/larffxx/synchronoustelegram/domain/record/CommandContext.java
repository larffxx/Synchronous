package com.larffxx.synchronoustelegram.domain.record;

import java.util.List;

public record CommandContext(
        Long chatId,
        String commandAuthorName,
        String commandName,
        List<String> options
) {
}
