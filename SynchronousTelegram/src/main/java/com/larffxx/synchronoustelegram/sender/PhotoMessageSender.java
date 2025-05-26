package com.larffxx.synchronoustelegram.sender;

import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.payload.DiscordPayload;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;

@Component
public class PhotoMessageSender implements PhotoSender {
    private final UpdateHolder updateHolder;
    private final ServersConnectDAO serversConnectDAO;

    public PhotoMessageSender(UpdateHolder updateHolder, ServersConnectDAO serversConnectDAO) {
        this.updateHolder = updateHolder;
        this.serversConnectDAO = serversConnectDAO;
    }

    public void sendPhoto(DiscordPayload payload) throws TelegramApiException{
        updateHolder.setChatId(serversConnectDAO.getTelegramChatByDiscordGuild(String.valueOf(payload.getGuildID())));
        Long chatID = Long.valueOf(updateHolder.getChatId());

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

            updateHolder.getTelegramClient().execute(sendPhoto);
        }
    }

    private void sendPhotoWithoutMessage(Long id, String author, List<File> files) throws TelegramApiException {
        for (File file : files) {
            SendPhoto sendPhoto = SendPhoto.builder()
                    .chatId(id)
                    .photo(new InputFile(file))
                    .caption("From: " + author)
                    .build();

            updateHolder.getTelegramClient().execute(sendPhoto);
        }
    }
}
