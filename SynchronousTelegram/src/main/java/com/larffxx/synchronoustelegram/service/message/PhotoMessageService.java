package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.sender.MediaSender;
import org.springframework.stereotype.Service;


/**
 * Message service that sends photo messages to Telegram chats.
 */
@Service
public class PhotoMessageService implements MessageService {
    /**
     * Receiver that provides the target chat for sending.
     */
    private final UpdateReceiver updateReceiver;
    /**
     * Sender that delivers media payloads to Telegram.
     */
    private final MediaSender mediaSender;

    /**
     * Creates a photo message service with its dependencies.
     *
     * @param updateReceiver receiver providing the target chat
     * @param mediaSender sender delivering media payloads
     */
    public PhotoMessageService(UpdateReceiver updateReceiver, MediaSender mediaSender) {
        this.updateReceiver = updateReceiver;
        this.mediaSender = mediaSender;
    }

    /**
     * Sends the photo described by the given context.
     *
     * @param messageContext classified message context to send
     */
    public void send(MessageContext messageContext) {
        boolean hasMessage = messageContext.getMessage() != null && !messageContext.getMessage().equals("null");
        String caption = hasMessage ? messageContext.getMessage() : "";
        messageContext.setMessage(caption);

        updateReceiver.setChatId(String.valueOf(messageContext.getTelegramChatId()));

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
