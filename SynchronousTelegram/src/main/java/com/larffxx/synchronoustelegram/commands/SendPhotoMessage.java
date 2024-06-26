package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class SendPhotoMessage implements Command{
    public void execute(UpdateReceiver update) {
        SendPhoto sendPhoto = SendPhoto.builder().chatId(String.valueOf(update.getChatId()))
                .photo(new InputFile()).caption("Here is your screen").parseMode("html").build();
        try {
            update.getTelegramClient().execute(sendPhoto);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public String getCommand() {
        return "/sendPhoto";
    }
}