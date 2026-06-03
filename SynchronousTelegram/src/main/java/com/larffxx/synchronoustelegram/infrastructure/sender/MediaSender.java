package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.record.PayloadContext;
import com.larffxx.synchronoustelegram.infrastructure.PayloadContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
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
    private final PayloadContextResolver payloadContextResolver;

    public MediaSender(UpdateReceiver updateReceiver, PayloadContextResolver payloadContextResolver) {
        this.updateReceiver = updateReceiver;
        this.payloadContextResolver = payloadContextResolver;
    }


    public void send(DiscordPayload discordPayload){
        PayloadContext payloadContext = payloadContextResolver.resolvePayloadContext(discordPayload);

        if(discordPayload.getFiles().size() >= 2){
            sendMedias(payloadContext);
        }else {
            for (File file : payloadContext.getFiles()) {
                SendPhoto sendPhoto = SendPhoto.builder()
                        .chatId(payloadContext.getChatID())
                        .photo(new InputFile(file))
                        .caption(payloadContext.getCaption())
                        .build();

                try {
                    updateReceiver.getTelegramClient().execute(sendPhoto);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void sendMedias(PayloadContext payloadContext){
        List<InputMedia> mediaList = new LinkedList<>();

        for (File file : payloadContext.getFiles()) {
            InputMediaPhoto media = new InputMediaPhoto(new InputFile(file).getNewMediaFile(), file.getName());
            media.setCaption(payloadContext.getCaption());

            mediaList.add(media);
        }
        SendMediaGroup sendMediaGroup = SendMediaGroup.builder()
                .chatId(payloadContext.getChatID())
                .medias(mediaList)
                .build();

        try {
            updateReceiver.getTelegramClient().execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
