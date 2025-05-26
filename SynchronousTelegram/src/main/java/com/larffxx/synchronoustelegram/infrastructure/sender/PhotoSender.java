package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface PhotoSender{
    void sendPhoto(DiscordPayload payload) throws TelegramApiException;
}
