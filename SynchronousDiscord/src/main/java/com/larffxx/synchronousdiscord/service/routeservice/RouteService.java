package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Base routing service that carries shared context and repositories.
 */
@Component
@Getter
@Setter
public abstract class RouteService <T> {
    /**
     * Shared context holding the current channel.
     */
    private final EventContext eventContext;
    /**
     * Repository for Discord to Telegram server connections.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates a routing service with shared dependencies.
     *
     * @param eventContext shared event context
     * @param serversConnectRepository repository for server connections
     */
    public RouteService(EventContext eventContext, ServersConnectRepository serversConnectRepository) {
        this.eventContext = eventContext;
        this.serversConnectRepository = serversConnectRepository;
    }
    /**
     * Routes the given context to its destination.
     *
     * @param t context to route
     * @throws RouteServiceException when routing fails
     */
    public abstract void send(T t) throws RouteServiceException;
}
