package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.exception.command.NoOptionsProvidedException;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Forwards a coolness rating to Discord, where it is applied.
 */
@Service
public class CoolCommand implements Command {
    private final TextMessageService textMessageService;

    /**
     * Creates a cool command.
     * @param textMessageService service for sending text replies
     */
    public CoolCommand(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    /**
     * Acknowledges the rating, the points are applied by the Discord module.
     * @param commandContext parsed command invocation context
     * @throws NoOptionsProvidedException if no Discord name is provided
     */
    @Override
    public void execute(CommandContext commandContext) throws TelegramApiException {
        if (commandContext.options().isEmpty()) {
            throw new NoOptionsProvidedException(InfExcMessage.NO_OPTIONS_FOR_COMMAND_EXCEPTION);
        }
        textMessageService.send(commandContext.chatId(), CommandConstant.COOLNESS_SENT);
    }

    /**
     * Returns the command name.
     * @return command name without leading slash
     */
    @Override
    public String getCommand() {
        return "cool";
    }
}
