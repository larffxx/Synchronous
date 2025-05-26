package com.larffxx.synchronoustelegram.executor;

import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.exception.TelegramException;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.model.UsersConnect;
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
    private final UpdateHolder updateHolder;

    public TelegramClientCommandExecutor(UpdateHolder updateHolder, CommandPreProcessor commandPreProcessor) {
        this.updateHolder = updateHolder;
        this.commandPreProcessor = commandPreProcessor;
    }

    public void execute(Update update){
        String[] s = update.getMessage().getText().split(" ");
        Command command = commandPreProcessor.getCommand(s[0]);

        try {
            command.execute(updateHolder);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }
}
