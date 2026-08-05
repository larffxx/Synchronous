package com.larffxx.synchronoustelegram.infrastructure.handler.filehandler;

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


@Getter
@Setter
@Component
public class SinglePhotoMessageHandler {
    private final PhotoDownloadService photoDownloadService;
    private final TmpToJpgConverter tmpToJpgConverter;
    private final ServersConnectRepository serversConnectRepository;

    public SinglePhotoMessageHandler(PhotoDownloadService photoDownloadService, TmpToJpgConverter tmpToJpgConverter, ServersConnectRepository serversConnectRepository) {
        this.photoDownloadService = photoDownloadService;
        this.tmpToJpgConverter = tmpToJpgConverter;
        this.serversConnectRepository = serversConnectRepository;
    }

    public MessagePayload handlePhotoMessage(Update update) {
        UpdateToMessagePayloadMapper updateToMessagePayloadMapper = new UpdateToMessagePayloadMapper();
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String guildId = serversConnectRepository.findByTelegramChannel(String.valueOf(chatId)).getDiscordGuild();
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
