package com.larffxx.synchronoustelegram.receivers;

import com.larffxx.synchronoustelegram.handler.MessageHandler;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;


@Getter
@Setter
@Component
public class MessageReceiver {
    private final MessageHandler messageHandler;
    private final UpdateReceiver updateReceiver;

    public MessageReceiver(MessageHandler messageHandler, UpdateReceiver updateReceiver) {
        this.messageHandler = messageHandler;
        this.updateReceiver = updateReceiver;
    }


    public void receiveMessage(Update update) {
        updateReceiver.saveUpdateToUpdateHolder(update);

        if(!update.getMessage().getFrom().getIsBot()){
            messageHandler.handleMessage(update);
        }
    }
}
