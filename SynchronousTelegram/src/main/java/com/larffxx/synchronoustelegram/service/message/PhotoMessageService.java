package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.sender.MediaSender;
import org.springframework.stereotype.Service;


/**
 * Message service that sends photo messages to Telegram chats.
 */
@Service
public class PhotoMessageService implements MessageService {
    /**
     * Sender that delivers media payloads to Telegram.
     */
    private final MediaSender mediaSender;

    /**
     * Creates a photo message service with its dependencies.
     *
     * @param mediaSender sender delivering media payloads
     */
    public PhotoMessageService(MediaSender mediaSender) {
        this.mediaSender = mediaSender;
    }

    /**
     * Sends the photo described by the given context.
     *
     * @param messageContext classified message context to send
     */
    public void send(MessageContext messageContext) {
        String rawMessage = messageContext.getMessage();
        boolean hasMessage = rawMessage != null && !rawMessage.isBlank() && !"null".equals(rawMessage);
        String caption = hasMessage ? rawMessage : "";
        messageContext.setMessage(caption);

        mediaSender.send(messageContext);
    }

    /**
     * Returns the message type handled by this service.
     *
     * @return photo message type identifier
     */
    @Override
    public String getType() {
        return "PHOTO_MESSAGE";
    }
}
