package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendTextMessage extends Command {
    public SendTextMessage(UpdateHolder updateHolder) {
        super(updateHolder);
    }

    public void execute(UpdateHolder update) throws TelegramApiException {
        SendMessage sm = SendMessage.builder().chatId(String.valueOf(update.getChatId())).text("Text").build();

        update.getTelegramClient().execute(sm);
    }

    public void execute(Long id, String text) throws TelegramApiException {
        SendMessage sm = SendMessage.builder().chatId(id).text(text).build();

        getUpdateHolder().getTelegramClient().execute(sm);
    }

    public String getCommand() {
        return "/sendText";
    }
}