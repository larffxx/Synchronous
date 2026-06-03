package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.record.PayloadContext;
import com.larffxx.synchronoustelegram.infrastructure.PayloadContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.sender.MediaSender;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import org.springframework.stereotype.Service;


@Service
public class PhotoMessageService implements MessageService {
    private final PayloadContextResolver payloadContextResolver;
    private final UpdateReceiver updateReceiver;
    private final MediaSender mediaSender;

    public PhotoMessageService(PayloadContextResolver payloadContextResolver, UpdateReceiver updateReceiver, MediaSender mediaSender) {
        this.payloadContextResolver = payloadContextResolver;
        this.updateReceiver = updateReceiver;
        this.mediaSender = mediaSender;
    }

    public void send(DiscordPayload payload) {
        PayloadContext payloadContext = payloadContextResolver.resolvePayloadContext(payload);

        updateReceiver.setChatId(payloadContext.getChatID());

        mediaSender.send(payload);
    }

    @Override
    public String getType() {
        return "PHOTO_MESSAGE";
    }
}
