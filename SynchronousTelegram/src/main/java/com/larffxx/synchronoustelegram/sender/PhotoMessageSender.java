package com.larffxx.synchronoustelegram.sender;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Component
public class PhotoMessageSender {
    private final UpdateHolder updateHolder;

    public PhotoMessageSender(UpdateHolder updateHolder) {
        this.updateHolder = updateHolder;
    }

    public void send(Long id, String message, List<File> uris) {

    }

    public void send(Long id, List<File> files) throws TelegramApiException {
        for (File file : files) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(id)
                    .photo(new InputFile(file))
                    .build();

            updateHolder.getTelegramClient().execute(sendPhoto);
        }
    }
}
