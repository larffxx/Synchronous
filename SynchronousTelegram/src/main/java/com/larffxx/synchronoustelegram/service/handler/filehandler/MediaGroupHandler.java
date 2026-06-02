package com.larffxx.synchronoustelegram.service.handler.filehandler;

import com.larffxx.synchronoustelegram.util.PhotoAlbumHolder;
import com.larffxx.synchronoustelegram.service.handler.utility.PhotoDownloadService;
import com.larffxx.synchronoustelegram.util.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.domain.exception.data.input.ReceivingPhotoException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Component
public class MediaGroupHandler {
    private final TmpToJpgConverter tmpToJpgConverter;
    private final PhotoDownloadService photoDownloadService;
    private final PhotoAlbumHolder photoAlbumHolder;

    public MediaGroupHandler(TmpToJpgConverter tmpToJpgConverter, PhotoDownloadService photoDownloadService, PhotoAlbumHolder photoAlbumHolder) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.photoDownloadService = photoDownloadService;
        this.photoAlbumHolder = photoAlbumHolder;
    }

    public MessagePayload handleAlbum(Update update){
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String userName = message.getFrom().getUserName();
        String caption = message.getCaption();
        String mediaGroupId = message.getMediaGroupId();
        List<PhotoSize> photos = message.getPhoto();

        Optional<PhotoSize> lastDownloadedPhotoFromAlbum = photoAlbumHolder.getPhoto(mediaGroupId, photos);

        if(lastDownloadedPhotoFromAlbum.isEmpty()){
            throw new ReceivingPhotoException(String.format(InfExcMessage.RECEIVING_PHOTO_FROM_MEDIA_GROUP_EXCEPTION, mediaGroupId));
        }
        File photoFile = tmpToJpgConverter.tmpConvertToJpg(photoDownloadService.downloadPhoto(lastDownloadedPhotoFromAlbum.get().getFileId()), Long.valueOf(message.getMessageId()));

        photoAlbumHolder.clearAllAlbumPhotos();
        if(caption == null){
            return new MessagePayload(chatId,userName,photoFile, String.valueOf(MessageType.PHOTO_MESSAGE));
        }
        return new MessagePayload(chatId,userName,caption,photoFile,String.valueOf(MessageType.PHOTO_MESSAGE));
    }

}
