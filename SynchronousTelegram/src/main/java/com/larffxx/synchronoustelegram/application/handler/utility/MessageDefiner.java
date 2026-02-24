package com.larffxx.synchronoustelegram.application.handler.utility;

import com.larffxx.synchronoustelegram.util.constant.MessageType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class MessageDefiner {
    public String defineMessage(Update update) {
        if(update.getMessage().hasPhoto()){
            return MessageType.photoMessage;
        }else {
            return MessageType.textMessage;
        }
    }
}
