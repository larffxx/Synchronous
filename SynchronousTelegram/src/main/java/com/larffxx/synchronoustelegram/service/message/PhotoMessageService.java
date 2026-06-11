package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.sender.MediaSender;
import org.springframework.stereotype.Service;


@Service
public class PhotoMessageService implements MessageService {
    private final UpdateReceiver updateReceiver;
    private final MediaSender mediaSender;

    public PhotoMessageService(UpdateReceiver updateReceiver, MediaSender mediaSender) {
        this.updateReceiver = updateReceiver;
        this.mediaSender = mediaSender;
    }

    public void send(MessageContext messageContext) {
        boolean hasMessage = messageContext.getMessage() != null && !messageContext.getMessage().equals("null");
        String caption = hasMessage ? messageContext.getMessage() : "";
        messageContext.setMessage(caption);

        updateReceiver.setChatId(String.valueOf(messageContext.getTelegramChatId()));

        mediaSender.send(messageContext);
    }

    @Override
    public String getType() {
        return "PHOTO_MESSAGE";
    }
}
