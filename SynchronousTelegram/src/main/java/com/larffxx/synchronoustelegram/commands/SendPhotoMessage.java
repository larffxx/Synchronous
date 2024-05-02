package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Component
public class SendPhotoMessage implements Command<SendPhoto> {
    public SendPhoto execute(UpdateReceiver update) {
        SendPhoto sendPhoto = SendPhoto.builder().chatId(String.valueOf(update.getChatId()))
                .photo(new InputFile()).caption("Here is your screen").parseMode("html").build();
        return sendPhoto;
    }

    public String getCommand() {
        return "/sendPhoto";
    }

    public SendPhotoMessage() {
    }
}