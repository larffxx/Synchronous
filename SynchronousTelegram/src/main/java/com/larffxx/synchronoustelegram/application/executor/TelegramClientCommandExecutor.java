package com.larffxx.synchronoustelegram.application.executor;

import com.larffxx.synchronoustelegram.application.commands.Command;
import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class TelegramClientCommandExecutor {
    private final CommandPreProcessor commandPreProcessor;
    private final  UpdateHandler updateHandler;

    public TelegramClientCommandExecutor(CommandPreProcessor commandPreProcessor, UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
        this.commandPreProcessor = commandPreProcessor;
    }

    public void execute(Update update){
        String[] s = update.getMessage().getText().split(" ");
        Command command = commandPreProcessor.getCommand(s[0]);

        try {
            command.execute(updateHandler);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
