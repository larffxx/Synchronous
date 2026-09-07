package com.larffxx.synchronoustelegram.infrastructure.handler.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.handler.filehandler.MediaGroupHandler;
import com.larffxx.synchronoustelegram.infrastructure.handler.filehandler.SinglePhotoMessageHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.MessagePayload;
import com.larffxx.synchronoustelegram.infrastructure.producer.TelegramKafkaMessageProducer;
import com.larffxx.synchronoustelegram.domain.constant.MessageType;
import com.larffxx.synchronoustelegram.service.registry.MessageServiceRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Routes photo updates to album or single photo handling and delivers synced photo messages.
 * Splits incoming media group updates from standalone photos before publishing to Kafka.
 */
@Setter
@Getter
@Component
public class PhotoMessageHandler implements MessageHandler {
    /**
     * Registry that looks up message services by type.
     */
    private final MessageServiceRegistry messageServiceRegistry;
    /**
     * Producer that forwards photo payloads to Kafka.
     */
    private final TelegramKafkaMessageProducer telegramKafkaMessageProducer;
    /**
     * Handler for photo albums sent as media groups.
     */
    private final MediaGroupHandler mediaGroupHandler;
    /**
     * Handler for standalone photo messages.
     */
    private final SinglePhotoMessageHandler singlePhotoMessageHandler;

    /**
     * Creates the handler with its collaborators.
     * @param messageServiceRegistry registry for message services
     * @param mediaGroupHandler handler for photo albums
     * @param singlePhotoMessageHandler handler for standalone photos
     * @param telegramKafkaMessageProducer producer for photo payloads
     */
    public PhotoMessageHandler(MessageServiceRegistry messageServiceRegistry, MediaGroupHandler mediaGroupHandler, SinglePhotoMessageHandler singlePhotoMessageHandler, TelegramKafkaMessageProducer telegramKafkaMessageProducer) {
        this.messageServiceRegistry = messageServiceRegistry;
        this.mediaGroupHandler = mediaGroupHandler;
        this.singlePhotoMessageHandler = singlePhotoMessageHandler;
        this.telegramKafkaMessageProducer = telegramKafkaMessageProducer;
    }

    /**
     * Receives a photo update and publishes the resulting payload to Kafka.
     * @param update the Telegram update carrying the photo
     */
    @Override
    public void handle(Update update){
        telegramKafkaMessageProducer.sendKafkaMessage(receivePayload(update));
    }

    /**
     * Sends a synced photo message through the registered photo message service.
     * @param messageContext the synced message context
     */
    @Override
    public void handle(MessageContext messageContext) {
        messageServiceRegistry.get(String.valueOf(MessageType.PHOTO_MESSAGE)).send(messageContext);
    }

    /**
     * Returns the handler type for photo messages.
     * @return the photo message type
     */
    @Override
    public String getType() {
        return String.valueOf(MessageType.PHOTO_MESSAGE);
    }

    /**
     * Builds a message payload from a photo update, using album handling for media groups.
     * @param update the Telegram update carrying the photo
     * @return the mapped message payload
     */
    private MessagePayload receivePayload(Update update){
        org.telegram.telegrambots.meta.api.objects.message.Message message = update.getMessage();

        if(message.getMediaGroupId() != null){
            return mediaGroupHandler.handleAlbum(update);
        }
        return singlePhotoMessageHandler.handlePhotoMessage(update);
    }
}
