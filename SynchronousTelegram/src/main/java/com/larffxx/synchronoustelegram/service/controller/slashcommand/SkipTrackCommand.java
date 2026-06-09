package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.controller.Command;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SkipTrackCommand implements Command {
    private final TextMessageService textMessageService;

    public SkipTrackCommand(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(CommandContext commandContext) throws TelegramApiException {
        textMessageService.send(commandContext.chatId(), "Skipped");
    }

    @Override
    public void execute(DiscordPayload discordPayload) throws TelegramApiException {
        textMessageService.send(discordPayload);
    }

    @Override
    public String getCommand() {
        return "skip";
    }
}
