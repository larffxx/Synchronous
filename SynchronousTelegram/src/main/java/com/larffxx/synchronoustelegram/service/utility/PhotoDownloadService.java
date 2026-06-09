package com.larffxx.synchronoustelegram.service.utility;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.data.input.AttachmentsDownloadException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Service
public class PhotoDownloadService {
    private final UpdateReceiver updateReceiver;

    public PhotoDownloadService(UpdateReceiver updateHolder) {
        this.updateReceiver = updateHolder;
    }

    public java.io.File downloadPhoto(String fileID) {
        TelegramClient telegramClient = updateReceiver.getTelegramClient();

        try {
            File file = telegramClient.execute(new GetFile(fileID));

            return telegramClient.downloadFile(file);
        } catch (TelegramApiException e) {
            throw new AttachmentsDownloadException(InfExcMessage.DOWNLOAD_IMAGE_EXCEPTION);
        }
    }
}
