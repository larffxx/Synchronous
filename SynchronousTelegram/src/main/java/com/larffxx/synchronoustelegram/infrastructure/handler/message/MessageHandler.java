package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void handle(Update update);
    void handle(DiscordPayload discordPayload);
    String getType();
}
