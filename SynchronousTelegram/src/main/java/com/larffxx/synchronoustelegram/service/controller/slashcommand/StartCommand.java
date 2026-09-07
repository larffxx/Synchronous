package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.constant.infmsg.CommandConstant;
import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import org.springframework.stereotype.Service;

/**
 * Slash command that sends the bot welcome message.
 */
@Service
public class StartCommand implements Command {
    /**
     * Service for sending text replies to Telegram chats.
     */
    private final TextMessageService textMessageService;

    /**
     * Creates a start command with its dependencies.
     *
     * @param textMessageService service for sending text replies
     */
    public StartCommand(TextMessageService textMessageService) {
        this.textMessageService = textMessageService;
    }

    /**
     * Sends the welcome message to the chat from the context.
     *
     * @param commandContext parsed command invocation context
     */
    @Override
    public void execute(CommandContext commandContext) {
        textMessageService.send(commandContext.chatId(), CommandConstant.START_MESSAGE);
    }

    /**
     * Returns the command name.
     *
     * @return command name without leading slash
     */
    @Override
    public String getCommand() {
        return "start";
    }
}
