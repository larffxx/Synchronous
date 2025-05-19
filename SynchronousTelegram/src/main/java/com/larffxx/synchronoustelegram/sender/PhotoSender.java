package com.larffxx.synchronoustelegram.sender;

import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface PhotoSender{
    void sendPhoto(DiscordPayload payload) throws TelegramApiException;
}
