package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
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
    private final UpdateHandler updateHandler;
    private final ServersConnectRepository serversConnectRepository;

    public PhotoMessageSender(UpdateHandler updateHandler, ServersConnectRepository serversConnectRepository) {
        this.updateHandler = updateHandler;
        this.serversConnectRepository = serversConnectRepository;
    }

    public void sendPhoto(DiscordPayload payload) throws TelegramApiException{
        updateHandler.setChatId(serversConnectRepository.findByDiscordGuild(String.valueOf(payload.getGuildID())).getTelegramChannel());
        Long chatID = Long.valueOf(updateHandler.getChatId());

        if (payload.getMessage() == null || payload.getMessage().equals("null")) {
            sendPhotoWithoutMessage(chatID, payload.getAuthor(), payload.getFiles());
        }else {
            sendPhotoWithMessage(chatID, payload.getAuthor(), payload.getMessage(), payload.getFiles());
        }
    }

    private void sendPhotoWithMessage(Long id, String author, String message, List<File> files) throws TelegramApiException {
        for(File file : files) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(id)
                    .photo(new InputFile(file))
                    .caption("From: " + author + "\n" + "Message: " + message)
                    .build();

            updateHandler.getTelegramClient().execute(sendPhoto);
        }
    }

    private void sendPhotoWithoutMessage(Long id, String author, List<File> files) throws TelegramApiException {
        for (File file : files) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(id)
                    .photo(new InputFile(file))
                    .caption("From: " + author)
                    .build();

            updateHandler.getTelegramClient().execute(sendPhoto);
        }
    }
}
