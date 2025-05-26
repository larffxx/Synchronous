package com.larffxx.synchronoustelegram.application.handler.filehandler;

import com.larffxx.synchronoustelegram.application.handler.utility.PhotoAlbumHolder;
import com.larffxx.synchronoustelegram.application.handler.utility.PhotoDownloader;
import com.larffxx.synchronoustelegram.application.handler.utility.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.util.constant.MessageType;
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
    private final PhotoDownloader photoDownloader;
    private final PhotoAlbumHolder photoAlbumHolder;

    public MediaGroupHandler(TmpToJpgConverter tmpToJpgConverter, PhotoDownloader photoDownloader, PhotoAlbumHolder photoAlbumHolder) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.photoDownloader = photoDownloader;
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
            //TODO: custom exception for downloading from media group
            throw new RuntimeException("No photo found for media group " + mediaGroupId);
        }
        File photoFile = tmpToJpgConverter.tmpConvertToJpg(photoDownloader.downloadPhoto(lastDownloadedPhotoFromAlbum.get().getFileId()), Long.valueOf(message.getMessageId()));

        photoAlbumHolder.clearAllAlbumPhotos();
        if(caption == null){
            return new MessagePayload(chatId,userName,photoFile, MessageType.photoMessage);
        }
        return new MessagePayload(chatId,userName,caption,photoFile,MessageType.photoMessage);
    }

}
