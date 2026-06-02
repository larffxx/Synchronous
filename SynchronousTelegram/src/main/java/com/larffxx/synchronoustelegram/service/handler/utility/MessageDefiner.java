package com.larffxx.synchronoustelegram.service.handler.utility;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageDefiner {
    public String defineMessage(Update update) {
        if(update.getMessage().hasPhoto()){
            return String.valueOf(MessageType.PHOTO_MESSAGE);
        }else {
            return String.valueOf(MessageType.TEXT_MESSAGE);
        }
    }
}
