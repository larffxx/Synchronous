package com.larffxx.synchronoustelegram.listeners;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Getter
@Setter
@NoArgsConstructor
public class CommandListener {
    private UpdateReceiver updateReceiver;

    @Autowired
    public CommandListener(UpdateReceiver updateReceiver) {
        this.updateReceiver = updateReceiver;
    }

    public void saveToUpdateReceiver(Update update) {
        updateReceiver.setUpdate(update);
        updateReceiver.setChatId(String.valueOf(update.getMessage().getChatId()));
    }
}
