package com.larffxx.synchronousdiscord.domain.service.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.domain.exception.RouteServiceException;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import com.larffxx.synchronousdiscord.infrastructure.repo.ServersConnectRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class RouteService {
    private final EventReceiver eventReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public RouteService(EventReceiver eventReceiver, ServersConnectRepository serversConnectRepository) {
        this.eventReceiver = eventReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }
    public abstract void send(JsonNode data) throws RouteServiceException;
}
