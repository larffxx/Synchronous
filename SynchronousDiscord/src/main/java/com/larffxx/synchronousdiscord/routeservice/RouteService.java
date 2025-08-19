package com.larffxx.synchronousdiscord.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receiver.EventReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public abstract class RouteService<T> {
    private final PreProcessor<T> preProcessor;
    private final EventReceiver eventReceiver;
    private final ServersConnectDAO serversConnectDAO;

    public RouteService(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, PreProcessor<T> preProcessor) {
        this.eventReceiver = eventReceiver;
        this.serversConnectDAO = serversConnectDAO;
        this.preProcessor = preProcessor;
    }
    public abstract void send(JsonNode data) throws CommandException;
}
