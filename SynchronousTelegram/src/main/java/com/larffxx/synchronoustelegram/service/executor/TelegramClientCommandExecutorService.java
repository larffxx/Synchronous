package com.larffxx.synchronoustelegram.service.executor;

import com.larffxx.synchronoustelegram.domain.exception.execution.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.CommandContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.controller.Command;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.service.registry.CommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Service
public class TelegramClientCommandExecutorService {
    private final CommandRegistry commandRegistry;
    private final CommandContextResolver commandContextResolver;

    public TelegramClientCommandExecutorService(CommandRegistry commandRegistry, CommandContextResolver commandContextResolver) {
        this.commandRegistry = commandRegistry;
        this.commandContextResolver = commandContextResolver;
    }

    public void execute(Update update){
        CommandContext commandContext = commandContextResolver.resolveCommandContext(update);

        Command command = commandRegistry.get(commandContext.commandName());
        try {
            command.execute(commandContext);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }

    public void execute(DiscordPayload payload){
        CommandPayload commandPayload = payload.getCommandPayload();

        Command command = commandRegistry.get(commandPayload.getCommand());
        try {
            command.execute(payload);
        } catch (TelegramApiException e) {
            throw new StringCommandExecutingException(InfExcMessage.WHILE_EXECUTE_STRING_COMMAND_EXCEPTION);
        }
    }
}
