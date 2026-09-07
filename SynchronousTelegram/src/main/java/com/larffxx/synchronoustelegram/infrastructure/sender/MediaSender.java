package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Sends photos and photo albums to Telegram chats.
 * Sends a media group when several files are attached and single photos otherwise.
 */
@Component
public class MediaSender implements Sender {
    /**
     * Receiver that exposes the Telegram client used for sending.
     */
    private final UpdateReceiver updateReceiver;

    /**
     * Creates the sender with the update receiver.
     * @param updateReceiver receiver exposing the Telegram client
     */
    public MediaSender(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }


    /**
     * Sends the files of the given message context to its Telegram chat.
     * @param messageContext the synced message context with attached files
     */
    public void send(MessageContext messageContext){

        if(messageContext.getFileList().size() >= 2){
            sendMedias(messageContext);
        }else {
            for (File file : messageContext.getFileList()) {
                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(messageContext.getTelegramChatId())
                        .photo(new InputFile(file))
                        .caption(messageContext.getMessage())
                        .build();

                try {
                    updateReceiver.getTelegramClient().execute(sendPhoto);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Sends all attached files as one Telegram media group.
     * @param messageContext the synced message context with attached files
     */
    private void sendMedias(MessageContext messageContext){
        List<InputMedia> mediaList = new LinkedList<>();

        for (File file : messageContext.getFileList()) {
            InputMediaPhoto media = new InputMediaPhoto(new InputFile(file).getNewMediaFile(), file.getName());
            media.setCaption(messageContext.getMessage());

            mediaList.add(media);
        }
        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .chatId(messageContext.getTelegramChatId())
                .medias(mediaList)
                .build();

        try {
            updateReceiver.getTelegramClient().execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
