package com.larffxx.synchronoustelegram.application.executor;

import com.larffxx.synchronoustelegram.application.commands.Command;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
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
    private final UpdateReceiver updateReceiver;

    public TelegramClientCommandExecutor(CommandPreProcessor commandPreProcessor, UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
        this.commandPreProcessor = commandPreProcessor;
    }

    public void execute(Update update){
        String[] s = update.getMessage().getText().split(" ");
        Command command = commandPreProcessor.get(s[0]);

        try {
            command.execute(updateReceiver);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
