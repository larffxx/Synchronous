package com.larffxx.synchronoustelegram.application.routeservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.larffxx.synchronoustelegram.application.commands.Command;
import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.domain.exception.StringCommandExecutingException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.parser.DiscordCommandPayloadParser;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
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
    private final CommandPreProcessor preProcessor;
    private final UpdateHandler updateHolder;

    public DiscordCommandRouteService(DiscordCommandPayloadParser discordCommandPayloadParser, CommandPreProcessor preProcessor, UpdateHandler updateHandler) {
        this.discordCommandPayloadParser = discordCommandPayloadParser;
        this.preProcessor = preProcessor;
        this.updateHolder = updateHandler;
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
