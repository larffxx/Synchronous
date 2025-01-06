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

    public void send(JsonNode data){
        String telegramChatID = data.findValue(CommandInfMessages.TELEGRAM_CHAT_ID).asText();

        getEventReceiver().setTextChannel(getEventReceiver().getJda()
                .getGuildById(getServersConnectDAO().getByTelegramChat(telegramChatID).getDiscordGuild())
                .getTextChannelsByName(CommandInfMessages.DISCORD_TEXT_CHANNEL,true).get(0));

        executeCommand(data);
    }

    private void executeCommand(JsonNode data){
        String strCommand = data.findValue(CommandInfMessages.COMMAND_VALUE).asText();

        Command command = getPreProcessor().getCommand(strCommand);

        try {
            command.execute(data);
        } catch (CommandException e) {
            System.out.println(e.getMessage());
        }
    }
}
