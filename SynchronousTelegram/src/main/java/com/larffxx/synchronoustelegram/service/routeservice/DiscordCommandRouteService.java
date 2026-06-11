package com.larffxx.synchronoustelegram.service.routeservice;

import com.larffxx.synchronoustelegram.domain.context.CommandContext;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.service.executor.TelegramClientCommandExecutorService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final TelegramClientCommandExecutorService telegramClientCommandExecutorService;

    public DiscordCommandRouteService(TelegramClientCommandExecutorService telegramClientCommandExecutorService) {
        this.telegramClientCommandExecutorService = telegramClientCommandExecutorService;
    }

    public void send(CommandContext commandContext){
        telegramClientCommandExecutorService.execute(commandContext);
    }

}
