package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateToCommandPayloadMapper implements PayloadMapper<CommandPayload> {

    @Override
    public CommandPayload mapToPayload(Update update, String guildId) {
        String[] parts = update.getMessage().getText().trim().split("\\s+");
        String command = parts[0].replace("/", "");
        String chatId = update.getMessage().getChatId().toString();
        String name = update.getMessage().getFrom().getUserName();

        List<String> listOfOptions = parts.length > 1
                ? new ArrayList<>(Arrays.asList(parts).subList(1, parts.length))
                : new ArrayList<>();

        return new CommandPayload(chatId,guildId, name, command, listOfOptions);
    }
}
