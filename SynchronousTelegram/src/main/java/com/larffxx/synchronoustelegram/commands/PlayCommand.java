package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
@Getter
@Setter
public class PlayCommand extends Command{
    public PlayCommand(UpdateReceiver updateReceiver) {
        super(updateReceiver);
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {
        SendMessage sm;
        sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("Music added").build();
        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
