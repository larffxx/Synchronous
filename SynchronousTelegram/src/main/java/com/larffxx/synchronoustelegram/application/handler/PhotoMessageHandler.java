package com.larffxx.synchronoustelegram.application.handler;

import com.larffxx.synchronoustelegram.application.handler.filehandler.MediaGroupHandler;
import com.larffxx.synchronoustelegram.application.handler.filehandler.SinglePhotoMessageHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.util.constant.MessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@Getter
@Component
public class PhotoMessageHandler implements MessageHandler {
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    private final MediaGroupHandler mediaGroupHandler;
    private final SinglePhotoMessageHandler singlePhotoMessageHandler;

    public PhotoMessageHandler(MediaGroupHandler mediaGroupHandler, SinglePhotoMessageHandler singlePhotoMessageHandler, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.mediaGroupHandler = mediaGroupHandler;
        this.singlePhotoMessageHandler = singlePhotoMessageHandler;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
    }

    @Override
    public void handle(Update update){
        telegramKafkaMessageProducer.sendKafkaMessage(receivePayload(update));
    }

    @Override
    public String getType() {
        return MessageType.photoMessage;
    }

    private MessagePayload receivePayload(Update update){
        org.telegram.telegrambots.meta.api.objects.message.Message message = update.getMessage();

        if(message.getMediaGroupId() != null){
            return mediaGroupHandler.handleAlbum(update);
        }
        return singlePhotoMessageHandler.onSinglePhotoReceived(update);
    }
}
