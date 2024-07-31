package com.larffxx.synchronousdiscord.routeservices;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronousdiscord.dao.ServersConnectDAO;
import com.larffxx.synchronousdiscord.exception.CommandException;
import com.larffxx.synchronousdiscord.infmsg.CommandInfMessages;
import com.larffxx.synchronousdiscord.preprocessor.PreProcessor;
import com.larffxx.synchronousdiscord.receivers.EventReceiver;
import com.larffxx.synchronousdiscord.slashcommands.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TelegramCommandRouteService extends RouteService<Command>{

    public TelegramCommandRouteService(EventReceiver eventReceiver, ServersConnectDAO serversConnectDAO, PreProcessor<Command> preProcessor) {
        super(eventReceiver, serversConnectDAO, preProcessor);
    }


    public void send(JsonNode data) throws CommandException {
        getEventReceiver().setTextChannel(getEventReceiver().getJda()
                .getGuildById(getServersConnectDAO().getByTelegramChat(data.findValue(CommandInfMessages.TELEGRAM_CHAT_ID).asText()).getDiscordGuild())
                .getTextChannelsByName(CommandInfMessages.DISCORD_TEXT_CHANNEL,true).get(0));
        Command command = getPreProcessor().getCommand(data.findValue(CommandInfMessages.COMMAND_VALUE).asText());

        command.execute(data);
    }

}
