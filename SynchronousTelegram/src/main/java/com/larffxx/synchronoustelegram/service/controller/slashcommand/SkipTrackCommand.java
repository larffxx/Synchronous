package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
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
        textMessageService.send(commandContext.chatId(), CommandConstant.MUSIC_SKIPPED);
    }

    @Override
    public String getCommand() {
        return "skip";
    }
}
