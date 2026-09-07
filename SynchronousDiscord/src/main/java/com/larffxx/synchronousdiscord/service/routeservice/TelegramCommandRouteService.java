package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.constant.infexc.InfExcMessages;
import com.larffxx.synchronousdiscord.domain.context.TelegramCommandContext;
import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.service.executor.CommandExecutor;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Routes incoming Telegram commands to the Discord command executor.
 */
@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService<TelegramCommandContext> {
    /**
     * Executor used to run the routed Telegram command.
     */
    private final CommandExecutor executor;

    /**
     * Creates a route service for Telegram commands.
     *
     * @param eventContext shared event context
     * @param executor executor for Telegram commands
     * @param serversConnectRepository repository for server connections
     */
    public TelegramCommandRouteService(EventContext eventContext, CommandExecutor executor, ServersConnectRepository serversConnectRepository) {
        super(eventContext,  serversConnectRepository);
        this.executor = executor;
    }

    /**
     * Sets the target channel and executes the Telegram command.
     *
     * @param telegramCommandContext Telegram command context to route
     * @throws RouteServiceException when command execution fails
     */
    public void send(TelegramCommandContext telegramCommandContext){
        getEventContext().setTextChannel(telegramCommandContext.textChannel());
        try {
            executor.execute(telegramCommandContext);
        } catch (RouteServiceException e) {
            throw new RouteServiceException(InfExcMessages.ROUTE_SERVICE_EXCEPTION);
        }
    }
}
