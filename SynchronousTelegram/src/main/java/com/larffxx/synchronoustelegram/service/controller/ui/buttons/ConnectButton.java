package com.larffxx.synchronoustelegram.service.controller.ui.buttons;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.controller.slashcommand.ConnectCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Button that delegates to the connect slash command.
 */
@Component
public class ConnectButton implements Button {
    /**
     * Slash command executed when the button is pressed.
     */
    private final ConnectCommand connectCommand;

    /**
     * Creates a connect button with its delegate command.
     *
     * @param connectCommand slash command handling the connection
     */
    public ConnectButton(ConnectCommand connectCommand) {
        this.connectCommand = connectCommand;
    }

    /**
     * Runs the connect command with the given button context.
     *
     * @param buttonContext parsed button invocation context
     * @throws TelegramApiException if the Telegram API call fails
     */
    @Override
    public void execute(CommandContext buttonContext) throws TelegramApiException {
        connectCommand.execute(buttonContext);
    }

    /**
     * Returns the button identifier.
     *
     * @return button identifier matching the connect command name
     */
    @Override
    public String getButton() {
        return connectCommand.getCommand();
    }

    /**
     * Reports whether the button requires extra user provided options.
     *
     * @return true because the Discord channel must be provided
     */
    @Override
    public boolean requiresOptions() {
        return true;
    }
}
