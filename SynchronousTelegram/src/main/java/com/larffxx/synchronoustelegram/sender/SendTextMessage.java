package com.larffxx.synchronoustelegram.sender;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendTextMessage {
    private final UpdateHolder updateHolder;

    public SendTextMessage(UpdateHolder updateHolder) {
        this.updateHolder = updateHolder;
    }

    public void send(Long id, String text) throws TelegramApiException {
        SendMessage sm = SendMessage.builder().chatId(id).text(text).build();

        updateHolder.getTelegramClient().execute(sm);
    }
}