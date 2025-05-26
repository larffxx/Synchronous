package com.larffxx.synchronoustelegram.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.exception.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.parser.DiscordCommandPayloadParser;
import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final DiscordCommandPayloadParser discordCommandPayloadParser;
    private final ServersConnectDAO serversConnectDAO;
    private final CommandPreProcessor preProcessor;
    private final UpdateHolder updateHolder;

    private final String GUILD_ID_JSON_NODE = "guildId";
    private final String COMMAND_JSON_NODE = "command";
    private final Character COMMAND_PREFIX = '/';

    public DiscordCommandRouteService(DiscordCommandPayloadParser discordCommandPayloadParser, ServersConnectDAO serversConnectDAO, CommandPreProcessor preProcessor, UpdateHolder updateHolder) {
        this.discordCommandPayloadParser = discordCommandPayloadParser;
        this.serversConnectDAO = serversConnectDAO;
        this.preProcessor = preProcessor;
        this.updateHolder = updateHolder;
    }

    public void send(JsonNode data){
        DiscordPayload payload = discordCommandPayloadParser.parseDiscordCommand(data);

        executeCommand(payload);
    }

    private void executeCommand(DiscordPayload payload){
        Command command = preProcessor.getCommand(payload.getCommand().getCommandName());
        try {
            command.execute(payload);
        } catch (TelegramApiException e) {
            throw new StringCommandExecutingException(InfExcMessage.WHILE_EXECUTE_STRING_COMMAND_EXCEPTION);
        }
    }
}
