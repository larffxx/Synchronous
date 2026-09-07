package com.larffxx.synchronoustelegram.service.routeservice;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Routes commands received from Discord to the Telegram command executor.
 */
@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    /**
     * Executor that runs Telegram slash commands.
     */
    private final TelegramClientCommandExecutorService telegramClientCommandExecutorService;

    /**
     * Creates a route service with its executor.
     *
     * @param telegramClientCommandExecutorService executor for Telegram commands
     */
    public DiscordCommandRouteService(TelegramClientCommandExecutorService telegramClientCommandExecutorService) {
        this.telegramClientCommandExecutorService = telegramClientCommandExecutorService;
    }

    /**
     * Forwards a Discord originated command to Telegram for execution.
     *
     * @param commandContext resolved command context to execute
     */
    public void send(CommandContext commandContext){
        telegramClientCommandExecutorService.execute(commandContext);
    }

}
