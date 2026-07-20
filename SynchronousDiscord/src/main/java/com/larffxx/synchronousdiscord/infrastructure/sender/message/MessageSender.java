package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.service.registry.SenderRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MessageSender {
    private final SenderRegistry senderRegistry;

    public MessageSender(SenderRegistry senderRegistry) {
        this.senderRegistry = senderRegistry;
    }

    public void sendMessage(TelegramMessageContext telegramMessageContext) {
        Sender sender = senderRegistry.getSender(telegramMessageContext.messageType().toString());

        sender.send(telegramMessageContext);
    }
}
