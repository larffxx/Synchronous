package com.larffxx.synchronoustelegram.infrastructure.mapper;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;

public class UpdateToMessagePayloadMapper implements PayloadMapper<MessagePayload> {

    @Override
    public MessagePayload mapToPayload(Update update, String guildId) {
        return mapToPayload(update, guildId, null);
    }

    public MessagePayload mapToPayload(Update update, String guildId, File photoFile) {
        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();

        if (photoFile != null) {
            return new MessagePayload(chatId, guildId, username, photoFile,
                    String.valueOf(MessageType.PHOTO_MESSAGE));
        }

        return new MessagePayload(chatId, guildId, username,
                update.getMessage().getText(),
                String.valueOf(MessageType.TEXT_MESSAGE));
    }
}
