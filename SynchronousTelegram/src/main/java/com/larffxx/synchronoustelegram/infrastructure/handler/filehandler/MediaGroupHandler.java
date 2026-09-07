package com.larffxx.synchronoustelegram.infrastructure.handler.filehandler;

import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
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

/**
 * Handles Telegram photo albums sent as media groups.
 * Downloads one representative photo of the album, converts it, and maps the update to a message payload.
 */
@Getter
@Setter
@Component
public class MediaGroupHandler {
    /**
     * Converter that turns downloaded photos into JPG files.
     */
    private final TmpToJpgConverter tmpToJpgConverter;
    /**
     * Service that downloads photos from Telegram servers.
     */
    private final PhotoDownloadService photoDownloadService;
    /**
     * Holder that tracks photos received within one media group.
     */
    private final PhotoAlbumHolder photoAlbumHolder;
    /**
     * Repository used to resolve the Discord guild linked to a Telegram channel.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates the handler with its collaborators.
     * @param tmpToJpgConverter converter for downloaded photos
     * @param photoDownloadService service for downloading photos
     * @param photoAlbumHolder holder tracking album photos
     * @param serversConnectRepository repository for server links
     */
    public MediaGroupHandler(TmpToJpgConverter tmpToJpgConverter, PhotoDownloadService photoDownloadService, PhotoAlbumHolder photoAlbumHolder, ServersConnectRepository serversConnectRepository) {
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.photoDownloadService = photoDownloadService;
        this.photoAlbumHolder = photoAlbumHolder;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Handles a media group update and builds a message payload from it.
     * @param update the Telegram update carrying one album photo
     * @return message payload for the album
     * @throws ReceivingPhotoException if no new photo of the album is available
     */
    public MessagePayload handleAlbum(Update update){
        UpdateToMessagePayloadMapper updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();

        Message message = update.getMessage();
        Long chatId = message.getChatId();
        ServersConnect serversConnect = serversConnectRepository.findByTelegramChannel(String.valueOf(chatId));
        if (serversConnect == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }
        String guildId = serversConnect.getDiscordGuild();
        String caption = message.getCaption();
        String mediaGroupId = message.getMediaGroupId();
        List<PhotoSize> photos = message.getPhoto();

        Optional<PhotoSize> lastDownloadedPhotoFromAlbum = photoAlbumHolder.getPhoto(mediaGroupId, photos);

        if(lastDownloadedPhotoFromAlbum.isEmpty()){
            throw new ReceivingPhotoException(String.format(InfExcMessage.RECEIVING_PHOTO_FROM_MEDIA_GROUP_EXCEPTION, mediaGroupId));
        }
        File photoFile = tmpToJpgConverter.tmpConvertToJpg(photoDownloadService.downloadPhoto(lastDownloadedPhotoFromAlbum.get().getFileId()), Long.valueOf(message.getMessageId()));

        MessagePayload messagePayload = updateToMessagePayloadMapper.mapToPayload(update, guildId, photoFile);
        photoAlbumHolder.removeAlbumPhotos(mediaGroupId);

        if(caption != null){
            messagePayload.setMessage(caption);
            return messagePayload;
        }
        return messagePayload;
    }

}
