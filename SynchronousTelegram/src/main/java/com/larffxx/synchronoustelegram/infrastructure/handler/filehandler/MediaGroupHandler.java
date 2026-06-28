package com.larffxx.synchronoustelegram.infrastructure.handler.filehandler;

import com.larffxx.synchronoustelegram.infrastructure.mapper.UpdateToMessagePayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.util.PhotoAlbumHolder;
import com.larffxx.synchronoustelegram.service.utility.PhotoDownloadService;
import com.larffxx.synchronoustelegram.util.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.domain.exception.data.input.ReceivingPhotoException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
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
    private final ServersConnectRepository serversConnectRepository;

    public MediaGroupHandler(TmpToJpgConverter tmpToJpgConverter, PhotoDownloadService photoDownloadService, PhotoAlbumHolder photoAlbumHolder, ServersConnectRepository serversConnectRepository) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.photoDownloadService = photoDownloadService;
        this.photoAlbumHolder = photoAlbumHolder;
        this.serversConnectRepository = serversConnectRepository;
    }

    public MessagePayload handleAlbum(Update update){
        UpdateToMessagePayloadMapper updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String guildId = serversConnectRepository.findByTelegramChannel(String.valueOf(chatId)).getDiscordGuild();
        String caption = message.getCaption();
        String mediaGroupId = message.getMediaGroupId();
        List<PhotoSize> photos = message.getPhoto();

        Optional<PhotoSize> lastDownloadedPhotoFromAlbum = photoAlbumHolder.getPhoto(mediaGroupId, photos);

        if(lastDownloadedPhotoFromAlbum.isEmpty()){
            throw new ReceivingPhotoException(String.format(InfExcMessage.RECEIVING_PHOTO_FROM_MEDIA_GROUP_EXCEPTION, mediaGroupId));
        }
        File photoFile = tmpToJpgConverter.tmpConvertToJpg(photoDownloadService.downloadPhoto(lastDownloadedPhotoFromAlbum.get().getFileId()), Long.valueOf(message.getMessageId()));

        MessagePayload messagePayload = updateToMessagePayloadMapper.mapToPayload(update, guildId, photoFile);
        photoAlbumHolder.clearAllAlbumPhotos();

        if(caption != null){
            messagePayload.setMessage(caption);
            return messagePayload;
        }
        return messagePayload;
    }

}
