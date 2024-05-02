package com.larffxx.synchronoustelegram.receivers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UpdateReceiver {
    private Update update;
    private String chatId;
}

