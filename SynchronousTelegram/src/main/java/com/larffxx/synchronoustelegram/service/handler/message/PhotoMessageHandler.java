package com.larffxx.synchronoustelegram.service.handler.message;

import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.service.handler.filehandler.MediaGroupHandler;
import com.larffxx.synchronoustelegram.service.handler.filehandler.SinglePhotoMessageHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.service.registry.MessageServiceRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Setter
@Getter
@Component
public class PhotoMessageHandler implements MessageHandler {
    private final MessageServiceRegistry messageServiceRegistry;
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    private final MediaGroupHandler mediaGroupHandler;
    private final SinglePhotoMessageHandler singlePhotoMessageHandler;

    public PhotoMessageHandler(MessageServiceRegistry messageServiceRegistry, MediaGroupHandler mediaGroupHandler, SinglePhotoMessageHandler singlePhotoMessageHandler, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.messageServiceRegistry = messageServiceRegistry;
        this.mediaGroupHandler = mediaGroupHandler;
        this.singlePhotoMessageHandler = singlePhotoMessageHandler;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
    }

    @Override
    public void handle(Update update){
        telegramKafkaMessageProducer.sendKafkaMessage(receivePayload(update));
    }

    @Override
    public void handle(DiscordPayload discordPayload) {
        messageServiceRegistry.get(String.valueOf(MessageType.PHOTO_MESSAGE)).send(discordPayload);
    }

    @Override
    public String getType() {
        return String.valueOf(MessageType.PHOTO_MESSAGE);
    }

    private MessagePayload receivePayload(Update update){
        org.telegram.telegrambots.meta.api.objects.message.Message message = update.getMessage();

        if(message.getMediaGroupId() != null){
            return mediaGroupHandler.handleAlbum(update);
        }
        return singlePhotoMessageHandler.onSinglePhotoReceived(update);
    }
}
