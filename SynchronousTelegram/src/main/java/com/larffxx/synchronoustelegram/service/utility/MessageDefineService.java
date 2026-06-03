package com.larffxx.synchronoustelegram.service.utility;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class MessageDefineService {
    public String defineMessage(Update update) {
        if(update.getMessage().hasPhoto()){
            return String.valueOf(MessageType.PHOTO_MESSAGE);
        }else {
            return String.valueOf(MessageType.TEXT_MESSAGE);
        }
    }
}
