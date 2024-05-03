package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendText implements Command{
    private final UpdateReceiver updateReceiver;

    public SendText(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    public void execute(UpdateReceiver update) {
        SendMessage sm = SendMessage.builder().chatId(String.valueOf(update.getChatId())).text("Text").build();
        try {
            update.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void execute(Long id, String text) {
        SendMessage sm = SendMessage.builder().chatId(id).text(text).build();
        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getCommand() {
        return "/sendText";
    }
}