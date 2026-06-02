package com.larffxx.synchronoustelegram.service.handler.filehandler;

import com.larffxx.synchronoustelegram.service.handler.utility.PhotoDownloadService;
import com.larffxx.synchronoustelegram.util.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;


@Getter
@Setter
@Component
public class SinglePhotoMessageHandler {
    private final PhotoDownloadService photoDownloadService;
    private final TmpToJpgConverter tmpToJpgConverter;

    public SinglePhotoMessageHandler(PhotoDownloadService photoDownloadService, TmpToJpgConverter tmpToJpgConverter) {
        this.photoDownloadService = photoDownloadService;
        this.tmpToJpgConverter = tmpToJpgConverter;
    }

    public MessagePayload onSinglePhotoReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String userName = message.getFrom().getUserName();
        String caption = message.getCaption();
        String getFileId = message.getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
        File photo = photoDownloadService.downloadPhoto(getFileId);

        if(caption != null){
            return new MessagePayload(chatId, userName, caption,tmpToJpgConverter.tmpConvertToJpg(photo, Long.valueOf(message.getMessageId())), String.valueOf(MessageType.PHOTO_MESSAGE));
        }
        return new MessagePayload(chatId,userName,tmpToJpgConverter.tmpConvertToJpg(photo, Long.valueOf(message.getMessageId())), String.valueOf(MessageType.PHOTO_MESSAGE));
    }
}
