package com.larffxx.synchronoustelegram.handler.filehandler;

import com.larffxx.synchronoustelegram.constant.MessageType;
import com.larffxx.synchronoustelegram.handler.utility.PhotoDownloader;
import com.larffxx.synchronoustelegram.handler.utility.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.payload.MessagePayload;
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
    private final PhotoDownloader photoDownloader;
    private final TmpToJpgConverter tmpToJpgConverter;

    public SinglePhotoMessageHandler(PhotoDownloader photoDownloader, TmpToJpgConverter tmpToJpgConverter) {
        this.photoDownloader = photoDownloader;
        this.tmpToJpgConverter = tmpToJpgConverter;
    }

    public MessagePayload onSinglePhotoReceived(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String userName = message.getFrom().getUserName();
        String caption = message.getCaption();
        String getFileId = message.getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();
        File photo = photoDownloader.downloadPhoto(getFileId);

        if(caption != null){
            return new MessagePayload(chatId, userName, caption,tmpToJpgConverter.tmpConvertToJpg(photo, Long.valueOf(message.getMessageId())), MessageType.photoMessage);
        }
        return new MessagePayload(chatId,userName,tmpToJpgConverter.tmpConvertToJpg(photo, Long.valueOf(message.getMessageId())), MessageType.photoMessage);
    }
}
