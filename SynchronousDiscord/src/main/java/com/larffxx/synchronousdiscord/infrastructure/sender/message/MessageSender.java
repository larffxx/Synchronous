package com.larffxx.synchronousdiscord.infrastructure.sender.message;

import com.larffxx.synchronousdiscord.domain.context.TelegramMessageContext;
import com.larffxx.synchronousdiscord.infrastructure.sender.Sender;
import com.larffxx.synchronousdiscord.service.registry.SenderRegistry;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Message Sender class.
 */
@Getter
@Setter
@Component
public class MessageSender {
    /**
     * The sender registry.
     */
    private final SenderRegistry senderRegistry;

    /**
     * Creates a new MessageSender.
     * @param senderRegistry the sender registry.
     */
    public MessageSender(SenderRegistry senderRegistry) {
        this.senderRegistry = senderRegistry;
    }

    /**
     * Sends message.
     * @param telegramMessageContext the telegram message context.
     */
    public void sendMessage(TelegramMessageContext telegramMessageContext) {
        Sender sender = senderRegistry.getSender(telegramMessageContext.messageType().toString());

        sender.send(telegramMessageContext);
    }
}
