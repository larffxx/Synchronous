package com.larffxx.synchronoustelegram.infrastructure.consumer;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcher;
import com.larffxx.synchronoustelegram.service.handler.utility.MessageDefiner;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TelegramMessageConsumer {
    private final UpdateReceiver updateReceiver;
    private final MessageDefiner messageDefiner;
    private final MessageDispatcher messageDispatcher;

    public TelegramMessageConsumer(MessageDefiner messageDefiner, UpdateReceiver updateReceiver, MessageDispatcher messageDispatcher) {
        this.messageDefiner = messageDefiner;
        this.updateReceiver = updateReceiver;
        this.messageDispatcher = messageDispatcher;
    }

    public void consumeMessage(Update update) {
        updateReceiver.receiveUpdate(update);

        if (update.getMessage().getFrom().getIsBot()) {
            return;
        }

        messageDispatcher.dispatch(update);
    }
}