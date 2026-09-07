package com.larffxx.synchronoustelegram.service.utility;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Classifies raw Telegram updates by message type.
 */
@Service
public class MessageDefineService {
    /**
     * Determines the message type of a raw Telegram update.
     *
     * @param update raw Telegram update to classify
     * @return message type identifier for the update
     */
    public String defineMessage(Update update) {
        if (update.hasCallbackQuery()) {
            return String.valueOf(MessageType.CALLBACK_QUERY);
        }
        if (update.hasMessage() && update.getMessage().hasPhoto()) {
            return String.valueOf(MessageType.PHOTO_MESSAGE);
        }
        return String.valueOf(MessageType.TEXT_MESSAGE);
    }
}
