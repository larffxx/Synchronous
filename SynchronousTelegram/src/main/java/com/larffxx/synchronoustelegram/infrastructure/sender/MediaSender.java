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

@Component
public class MediaSender implements Sender {
    private final UpdateReceiver updateReceiver;

    public MediaSender(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }


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
