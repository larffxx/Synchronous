package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.larffxx.synchronoustelegram.infrastructure.payload.CommandPayload;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Maps a Telegram command update to a command payload.
 * Splits the message text into the command name and its options.
 */
public class UpdateToCommandPayloadMapper implements PayloadMapper<CommandPayload> {

    /**
     * Converts the given update into a command payload.
     * @param update the Telegram update holding the command
     * @param guildId the Discord guild linked to the chat
     * @return the mapped command payload
     */
    @Override
    public CommandPayload mapToPayload(Update update, String guildId) {
        String[] parts = update.getMessage().getText().trim().split("\\s+");
        String command = parts[0].startsWith("/") ? parts[0].substring(1) : parts[0];
        String chatId = update.getMessage().getChatId().toString();
        String name = update.getMessage().getFrom().getUserName();

        List<String> listOfOptions = parts.length > 1
                ? new ArrayList<>(Arrays.asList(parts).subList(1, parts.length))
                : new ArrayList<>();

        return new CommandPayload(chatId,guildId, name, command, listOfOptions);
    }
}
