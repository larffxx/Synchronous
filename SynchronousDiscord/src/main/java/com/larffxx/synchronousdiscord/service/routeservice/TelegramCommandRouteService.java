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

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService<TelegramCommandContext> {
    private final CommandExecutor executor;

    public TelegramCommandRouteService(EventContext eventContext, CommandExecutor executor, ServersConnectRepository serversConnectRepository) {
        super(eventContext,  serversConnectRepository);
        this.executor = executor;
    }

    //TODO: 1 single route
    public void send(TelegramCommandContext telegramCommandContext){
        getEventContext().setTextChannel(telegramCommandContext.textChannel());
        try {
            executor.execute(telegramCommandContext);
        } catch (RouteServiceException e) {
            throw new RouteServiceException(InfExcMessages.ROUTE_SERVICE_EXCEPTION);
        }
    }
}
