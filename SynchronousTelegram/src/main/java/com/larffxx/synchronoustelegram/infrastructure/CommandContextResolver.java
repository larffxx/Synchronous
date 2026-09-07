package com.larffxx.synchronoustelegram.infrastructure;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Builds a command context from a Telegram text command.
 * Parses the command name and its whitespace separated options with a regular expression.
 */
@Component
public class CommandContextResolver {
    /**
     * Parses the message text of the given update into a command context.
     * @param update the Telegram update holding the command text
     * @return command context with the parsed command name and options
     * @throws InvalidCommandException if the text does not match the command pattern
     */
    public CommandContext resolveCommandContext(Update update){
        Pattern commandNamePattern = Pattern.compile("^/([a-zA-Z]+)(?:\\s+(.+))?$");
        Matcher commandNameMatcher = commandNamePattern.matcher(update.getMessage().getText());

        String commandName;
        List<String> options;
        List<String> response = new ArrayList<>();
        if (commandNameMatcher.matches()) {
            commandName = commandNameMatcher.group(1);
            options = commandNameMatcher.group(2) != null ? Arrays.asList(commandNameMatcher.group(2).split("\\s")) : new ArrayList<>();
        }else {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }

        return new CommandContext(update.getMessage().getChatId(),update.getMessage().getFrom().getUserName(), commandName, options, response);
    }
}
