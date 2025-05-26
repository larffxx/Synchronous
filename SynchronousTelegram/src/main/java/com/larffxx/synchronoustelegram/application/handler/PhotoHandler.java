package com.larffxx.synchronoustelegram.application.handler;

import com.larffxx.synchronoustelegram.application.handler.filehandler.MediaGroupHandler;
import com.larffxx.synchronoustelegram.application.handler.filehandler.SinglePhotoMessageHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

@Setter
@Getter
@Component
public class PhotoHandler {
    private final MediaGroupHandler mediaGroupHandler;
    private final SinglePhotoMessageHandler singlePhotoMessageHandler;

    public PhotoHandler(MediaGroupHandler mediaGroupHandler, SinglePhotoMessageHandler singlePhotoMessageHandler) {
        this.mediaGroupHandler = mediaGroupHandler;
        this.singlePhotoMessageHandler = singlePhotoMessageHandler;
    }

    public MessagePayload handlePhotoMessage(Update update){
        Message message = update.getMessage();

        if(message.getMediaGroupId() != null){
            return mediaGroupHandler.handleAlbum(update);
        }
        return singlePhotoMessageHandler.onSinglePhotoReceived(update);
    }
}
