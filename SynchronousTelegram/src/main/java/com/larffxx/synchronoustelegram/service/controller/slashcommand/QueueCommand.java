package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.controller.Command;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class QueueCommand implements Command {
    private final TextMessageService textMessageService;

    public QueueCommand(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(CommandContext commandContext) throws TelegramApiException {
        textMessageService.send(commandContext.chatId(), "Queue");
    }


    @Override
    public String getCommand() {
        return "queue";
    }
}
