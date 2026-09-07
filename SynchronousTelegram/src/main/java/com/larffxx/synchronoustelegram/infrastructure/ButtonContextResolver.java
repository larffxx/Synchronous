package com.larffxx.synchronoustelegram.infrastructure;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.ButtonConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a command context from a Telegram callback query.
 * Validates the button callback data prefix and extracts the button name.
 */
@Component
public class ButtonContextResolver {
    /**
     * Resolves a command context from the callback query of the given update.
     * @param update the Telegram update holding the callback query
     * @return command context describing the pressed button
     * @throws InvalidCommandException if the callback data is missing or has an unexpected format
     */
    public CommandContext resolveButtonContext(Update update) {
        CallbackQuery query = update.getCallbackQuery();

        if (query == null || query.getData() == null || !query.getData().startsWith(ButtonConstant.CALLBACK_PREFIX)) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }

        String buttonName = query.getData().substring(ButtonConstant.CALLBACK_PREFIX.length());
        if (buttonName.isBlank()) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }

        return new CommandContext(
                query.getMessage().getChatId(),
                query.getFrom().getUserName(),
                buttonName,
                List.of(),
                new ArrayList<>()
        );
    }
}
