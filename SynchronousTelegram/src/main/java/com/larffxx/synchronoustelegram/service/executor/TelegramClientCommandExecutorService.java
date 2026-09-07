package com.larffxx.synchronoustelegram.service.executor;

import com.larffxx.synchronoustelegram.domain.exception.execution.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.CommandContextResolver;
import com.larffxx.synchronoustelegram.service.controller.slashcommand.Command;
import com.larffxx.synchronoustelegram.domain.exception.TelegramException;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.service.registry.CommandRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Executes slash commands coming from the Telegram client.
 */
@Getter
@Setter
@Service
public class TelegramClientCommandExecutorService {
    /**
     * Registry of available slash commands.
     */
    private final CommandRegistry commandRegistry;
    /**
     * Resolver that builds a command context from a raw update.
     */
    private final CommandContextResolver commandContextResolver;

    /**
     * Creates a command executor with its dependencies.
     *
     * @param commandRegistry registry of available commands
     * @param commandContextResolver resolver for command contexts
     */
    public TelegramClientCommandExecutorService(CommandRegistry commandRegistry, CommandContextResolver commandContextResolver) {
        this.commandRegistry = commandRegistry;
        this.commandContextResolver = commandContextResolver;
    }

    /**
     * Resolves the command from the update and executes it.
     *
     * @param update raw Telegram update carrying the command
     * @throws InvalidCommandException if no command matches the context
     * @throws TelegramException if the Telegram API call fails
     */
    public void execute(Update update){
        CommandContext commandContext = commandContextResolver.resolveCommandContext(update);

        Command command = commandRegistry.get(commandContext.commandName());
        if (command == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }
        try {
            command.execute(commandContext);
        } catch (TelegramApiException e) {
            throw new TelegramException(e.getMessage());
        }
    }

    /**
     * Executes an already resolved command context.
     *
     * @param commandContext resolved command context to execute
     * @throws InvalidCommandException if no command matches the context
     * @throws StringCommandExecutingException if the Telegram API call fails
     */
    public void execute(CommandContext commandContext){
        Command command = commandRegistry.get(commandContext.commandName());
        if (command == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }
        try {
            command.execute(commandContext);
        } catch (TelegramApiException e) {
            throw new StringCommandExecutingException(InfExcMessage.WHILE_EXECUTE_STRING_COMMAND_EXCEPTION);
        }
    }
}
