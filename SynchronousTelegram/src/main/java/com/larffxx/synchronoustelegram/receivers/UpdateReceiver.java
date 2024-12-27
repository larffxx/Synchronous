package com.larffxx.synchronoustelegram.receivers;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
@Component
@NoArgsConstructor
public class UpdateReceiver {
    private UpdateHolder updateHolder;

    @Autowired
    public UpdateReceiver(UpdateHolder updateHolder) {
        this.updateHolder = updateHolder;
    }

    public void saveUpdateToUpdateHolder(Update update) {
        updateHolder.setUpdate(update);
        updateHolder.setChatId(String.valueOf(update.getMessage().getChatId()));
    }
}
