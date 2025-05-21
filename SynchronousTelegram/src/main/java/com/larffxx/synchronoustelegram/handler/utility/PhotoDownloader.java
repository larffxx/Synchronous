package com.larffxx.synchronoustelegram.handler.utility;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
public class PhotoDownloader {
    private final UpdateHolder updateHolder;

    public PhotoDownloader(UpdateHolder updateHolder) {
        this.updateHolder = updateHolder;
    }

    public java.io.File downloadPhoto(String fileID) {
        TelegramClient telegramClient = updateHolder.getTelegramClient();

        try {
            File file = telegramClient.execute(new GetFile(fileID));

            return telegramClient.downloadFile(file);
        } catch (TelegramApiException e) {
            //TODO: Custom exception for downloading photo
            throw new RuntimeException(e);
        }
    }
}
