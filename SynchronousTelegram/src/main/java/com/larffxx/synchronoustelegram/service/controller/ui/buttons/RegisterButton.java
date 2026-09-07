package com.larffxx.synchronoustelegram.service.controller.ui.buttons;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.controller.slashcommand.RegisterCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Button that delegates to the register slash command.
 */
@Component
public class RegisterButton implements Button {
    /**
     * Slash command executed when the button is pressed.
     */
    private final RegisterCommand registerCommand;

    /**
     * Creates a register button with its delegate command.
     *
     * @param registerCommand slash command handling the registration
     */
    public RegisterButton(RegisterCommand registerCommand) {
        this.registerCommand = registerCommand;
    }

    /**
     * Runs the register command with the given button context.
     *
     * @param buttonContext parsed button invocation context
     * @throws TelegramApiException if the Telegram API call fails
     */
    @Override
    public void execute(CommandContext buttonContext) throws TelegramApiException {
        registerCommand.execute(buttonContext);
    }

    /**
     * Returns the button identifier.
     *
     * @return button identifier matching the register command name
     */
    @Override
    public String getButton() {
        return registerCommand.getCommand();
    }

    /**
     * Reports whether the button requires extra user provided options.
     *
     * @return true because the Discord name must be provided
     */
    @Override
    public boolean requiresOptions() {
        return true;
    }
}
