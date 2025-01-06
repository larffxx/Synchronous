package com.larffxx.synchronoustelegram.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.commands.Command;
import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.preprocessors.CommandPreProcessor;
import com.larffxx.synchronoustelegram.preprocessors.PreProcessor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
@Component
public class DiscordCommandRouteService {
    private final ServersConnectDAO serversConnectDAO;
    private final CommandPreProcessor preProcessor;
    private final UpdateHolder updateHolder;

    private final String GUILD_ID_JSON_NODE = "guildId";
    private final String COMMAND_JSON_NODE = "command";
    private final Character COMMAND_PREFIX = '/';

    public DiscordCommandRouteService(ServersConnectDAO serversConnectDAO, CommandPreProcessor preProcessor, UpdateHolder updateHolder) {
        this.serversConnectDAO = serversConnectDAO;
        this.preProcessor = preProcessor;
        this.updateHolder = updateHolder;
    }

    public void send(JsonNode data){
        String guild = data.findValue(GUILD_ID_JSON_NODE).asText();
        String command = createStringCommandFromJson(data);

        updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(guild).getTelegramChannel());

        executeStringCommand(command);
    }

    private void executeStringCommand(String strCommand){
        Command command = preProcessor.getCommand(strCommand);
        try {
            command.execute(updateHolder);
        } catch (TelegramApiException e) {
            //TODO custom exception
            throw new RuntimeException(e);
        }
    }

    private String createStringCommandFromJson(JsonNode data){
        String jsonCommand = data.findValue(COMMAND_JSON_NODE).asText();
        StringBuilder builder = new StringBuilder(jsonCommand);

        builder.insert(0, COMMAND_PREFIX);

        return String.valueOf(builder);
    }
}
