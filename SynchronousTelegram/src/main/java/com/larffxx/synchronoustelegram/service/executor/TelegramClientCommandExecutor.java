package com.larffxx.synchronoustelegram.service.executor;

import com.larffxx.synchronoustelegram.domain.exception.execution.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.controller.Command;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.service.registry.CommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class TelegramClientCommandExecutor {
    private final CommandRegistry commandRegistry;
    private final UpdateReceiver updateReceiver;

    public TelegramClientCommandExecutor(CommandRegistry commandRegistry, UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
        this.commandRegistry = commandRegistry;
    }
    public void execute(Update update){
        String[] options = update.getMessage().getText().split(" ");

        Command command = commandRegistry.get(options[0]);

        updateReceiver.setOption(options[1]);

        try {
            command.execute(updateReceiver);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }

    public void execute(DiscordPayload payload){
        Command command = commandRegistry.get(payload.getCommand().getCommandName());
        try {
            command.execute(payload);
        } catch (TelegramApiException e) {
            throw new StringCommandExecutingException(InfExcMessage.WHILE_EXECUTE_STRING_COMMAND_EXCEPTION);
        }
    }
}
