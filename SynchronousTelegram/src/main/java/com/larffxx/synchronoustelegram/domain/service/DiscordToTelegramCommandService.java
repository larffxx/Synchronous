package com.larffxx.synchronoustelegram.domain.service;

import com.larffxx.synchronoustelegram.application.commands.Command;
import com.larffxx.synchronoustelegram.domain.exception.execution.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordToTelegramCommandService {
    private final CommandPreProcessor commandPreProcessor;

    public DiscordToTelegramCommandService(CommandPreProcessor commandPreProcessor) {
        this.commandPreProcessor = commandPreProcessor;
    }

    public void execute(DiscordPayload payload){
        Command command = commandPreProcessor.get(payload.getCommand().getCommandName());
        try {
            command.execute(payload);
        } catch (TelegramApiException e) {
            throw new StringCommandExecutingException(InfExcMessage.WHILE_EXECUTE_STRING_COMMAND_EXCEPTION);
        }
    }
}
