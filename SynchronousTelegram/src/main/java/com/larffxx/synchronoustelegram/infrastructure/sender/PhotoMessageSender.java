package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.execution.SendingPhotoException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Component
public class PhotoMessageSender implements PhotoSender {
    private final UpdateReceiver updateReceiver;
    private final ServersConnectRepository serversConnectRepository;

    public PhotoMessageSender(UpdateReceiver updateReceiver, ServersConnectRepository serversConnectRepository) {
        this.updateReceiver = updateReceiver;
        this.serversConnectRepository = serversConnectRepository;
    }

    public void sendPhoto(DiscordPayload payload) {
        updateReceiver.setChatId(serversConnectRepository.findByDiscordGuild(String.valueOf(payload.getGuildID())).getTelegramChannel());
        Long chatID = Long.valueOf(updateReceiver.getChatId());

        if (payload.getMessage() == null || payload.getMessage().equals("null")) {
            sendPhotoAlbumWithoutMessage(chatID, payload.getAuthor(), payload.getFiles());
        } else {
            sendPhotoAlbumWithMessage(chatID, payload.getAuthor(), payload.getMessage(), payload.getFiles());
        }
    }

    private void sendPhotoAlbumWithMessage(Long id, String author, String message, List<File> files) {
        try {
            for (File file : files) {
                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(id)
                        .photo(new InputFile(file))
                        .caption(String.format("From: %s\nMessage: %s", author, message))
                        .build();

                updateReceiver.getTelegramClient().execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            throw new SendingPhotoException(InfExcMessage.SENDING_ALBUM_WITH_MESSAGE_EXCEPTION);
        }
    }

    private void sendPhotoAlbumWithoutMessage(Long id, String author, List<File> files) {
        try {
            for (File file : files) {
                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(id)
                        .photo(new InputFile(file))
                        .caption(String.format("From: %s", author))
                        .build();

                updateReceiver.getTelegramClient().execute(sendPhoto);
            }
        } catch (TelegramApiException e) {
            throw new SendingPhotoException(InfExcMessage.SENDING_ALBUM_WITHOUT_MESSAGE_EXCEPTION);
        }
    }
}
