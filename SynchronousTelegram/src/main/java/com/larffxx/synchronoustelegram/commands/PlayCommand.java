package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public class PlayCommand extends Command{
    public PlayCommand(UpdateHolder updateHolder) {
        super(updateHolder);
    }

    @Override
    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        SendMessage sm;
        sm = SendMessage.builder().chatId(updateHolder.getChatId()).text("Music added").build();
        updateHolder.getTelegramClient().execute(sm);
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
