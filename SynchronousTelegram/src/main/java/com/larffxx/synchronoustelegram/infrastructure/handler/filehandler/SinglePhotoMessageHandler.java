package com.larffxx.synchronoustelegram.infrastructure.handler.filehandler;

import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.exception.command.InvalidCommandException;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.infrastructure.mapper.UpdateToMessagePayloadMapper;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.utility.PhotoDownloadService;
import com.larffxx.synchronoustelegram.util.TmpToJpgConverter;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;


/**
 * Handles Telegram updates that carry a single standalone photo.
 * Downloads the largest photo size, converts it, and maps the update to a message payload.
 */
@Getter
@Setter
@Component
public class SinglePhotoMessageHandler {
    /**
     * Service that downloads photos from Telegram servers.
     */
    private final PhotoDownloadService photoDownloadService;
    /**
     * Converter that turns downloaded photos into JPG files.
     */
    private final TmpToJpgConverter tmpToJpgConverter;
    /**
     * Repository used to resolve the Discord guild linked to a Telegram channel.
     */
    private final ServersConnectRepository serversConnectRepository;

    /**
     * Creates the handler with its collaborators.
     * @param photoDownloadService service for downloading photos
     * @param tmpToJpgConverter converter for downloaded photos
     * @param serversConnectRepository repository for server links
     */
    public SinglePhotoMessageHandler(PhotoDownloadService photoDownloadService, TmpToJpgConverter tmpToJpgConverter, ServersConnectRepository serversConnectRepository) {
        this.photoDownloadService = photoDownloadService;
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.serversConnectRepository = serversConnectRepository;
    }

    /**
     * Handles a single photo update and builds a message payload from it.
     * @param update the Telegram update carrying the photo
     * @return message payload with the converted photo attached
     */
    public MessagePayload handlePhotoMessage(Update update) {
        UpdateToMessagePayloadMapper updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        ServersConnect serversConnect = serversConnectRepository.findByTelegramChannel(String.valueOf(chatId));
        if (serversConnect == null) {
            throw new InvalidCommandException(InfExcMessage.INVALID_COMMAND_EXCEPTION);
        }
        String guildId = serversConnect.getDiscordGuild();
        String caption = message.getCaption();
        String getFileId = message.getPhoto().get(update.getMessage().getPhoto().size() - 1).getFileId();

        File rawPhoto = photoDownloadService.downloadPhoto(getFileId);
        File photo = tmpToJpgConverter.tmpConvertToJpg(rawPhoto, Long.valueOf(message.getMessageId()));

        MessagePayload messagePayload = updateToMessagePayloadMapper.mapToPayload(update, guildId, photo);
        if(caption != null){
            messagePayload.setMessage(caption);
            return messagePayload;
        }
        return messagePayload;
    }
}
