package com.larffxx.synchronousdiscord.service.routeservice;

import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.domain.context.EventContext;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class RouteService <T> {
    private final EventContext eventContext;
    private final ServersConnectRepository serversConnectRepository;

    public RouteService(EventContext eventContext, ServersConnectRepository serversConnectRepository) {
        this.eventContext = eventContext;
        this.serversConnectRepository = serversConnectRepository;
    }
    public abstract void send(T t) throws RouteServiceException;
}
