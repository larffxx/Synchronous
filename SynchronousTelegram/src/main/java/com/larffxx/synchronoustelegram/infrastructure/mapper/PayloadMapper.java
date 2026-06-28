package com.larffxx.synchronoustelegram.infrastructure.mapper;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface PayloadMapper <T> {
    T mapToPayload(Update update, String guildId);
}
