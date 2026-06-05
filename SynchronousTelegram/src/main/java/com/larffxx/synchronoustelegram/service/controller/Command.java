package com.larffxx.synchronoustelegram.service.controller;

import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

//TODO: Commands from Discord
public interface Command {
    void execute(CommandContext commandContext) throws TelegramApiException;
    void execute(DiscordPayload discordPayload) throws TelegramApiException;
    String getCommand();
}
