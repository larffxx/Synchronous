package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class SendText implements Command<SendMessage> {
    public SendText() {
    }

    public SendMessage execute(UpdateReceiver update) {
        SendMessage sm = SendMessage.builder().chatId(String.valueOf(update.getChatId())).text("Text").build();
        return sm;
    }

    public SendMessage execute(Long id, String text) {
        SendMessage sm = SendMessage.builder().chatId(id).text(text).build();
        return sm;
    }

    public String getCommand() {
        return "/sendText";
    }
}