package com.larffxx.synchronoustelegram.infrastructure.receiver;

import com.larffxx.synchronoustelegram.application.dispatcher.MessageDispatcher;
import com.larffxx.synchronoustelegram.application.handler.utility.MessageDefiner;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class MessageReceiver {
    private final UpdateReceiver updateReceiver;
    private final MessageDefiner messageDefiner;
    private final MessageDispatcher messageDispatcher;

    public MessageReceiver(MessageDefiner messageDefiner, UpdateReceiver updateReceiver, MessageDispatcher messageDispatcher) {
        this.messageDefiner = messageDefiner;
        this.updateReceiver = updateReceiver;
        this.messageDispatcher = messageDispatcher;
    }


    public void handleMessage(Update update) {
        updateReceiver.receiveUpdate(update);

        if (update.getMessage().getFrom().getIsBot()) {
            return;
        }

        messageDispatcher.dispatch(update);
    }
}