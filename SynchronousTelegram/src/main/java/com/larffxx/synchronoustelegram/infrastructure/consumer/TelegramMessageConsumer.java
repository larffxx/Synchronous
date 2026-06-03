package com.larffxx.synchronoustelegram.infrastructure.consumer;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.service.dispatcher.MessageDispatcherService;
import com.larffxx.synchronoustelegram.service.utility.MessageDefineService;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TelegramMessageConsumer {
    private final UpdateReceiver updateReceiver;
    private final MessageDefineService messageDefineService;
    private final MessageDispatcherService messageDispatcherService;

    public TelegramMessageConsumer(MessageDefineService messageDefineService, UpdateReceiver updateReceiver, MessageDispatcherService messageDispatcherService) {
        this.messageDefineService = messageDefineService;
        this.updateReceiver = updateReceiver;
        this.messageDispatcherService = messageDispatcherService;
    }

    public void consumeMessage(Update update) {
        updateReceiver.receiveUpdate(update);

        if (update.getMessage().getFrom().getIsBot()) {
            return;
        }

        messageDispatcherService.dispatch(update);
    }
}