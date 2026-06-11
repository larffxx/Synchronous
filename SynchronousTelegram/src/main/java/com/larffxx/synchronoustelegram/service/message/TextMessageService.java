package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;

@Component
public class TextMessageService implements MessageService {
    private final UpdateReceiver updateReceiver;
    private final TextMessageSender textMessageSender;

    public TextMessageService(UpdateReceiver updateReceiver, TextMessageSender textMessageSender) {
        this.updateReceiver = updateReceiver;
        this.textMessageSender = textMessageSender;
    }

    @Override
    public void send(MessageContext messageContext) {
        updateReceiver.setChatId(String.valueOf(messageContext.getTelegramChatId()));

        textMessageSender.send(messageContext);
    }

    public void send(Long chatId, String text) {
        textMessageSender.send(chatId, text);
    }

    @Override
    public String getType() {
        return "TEXT_MESSAGE";
    }
}
