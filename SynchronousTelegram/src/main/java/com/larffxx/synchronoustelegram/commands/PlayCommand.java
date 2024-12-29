package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.sender.TextMessageSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public class PlayCommand extends Command{
    private final TextMessageSender textMessageSender;

    public PlayCommand(UpdateHolder updateHolder, TextMessageSender textMessageSender) {
        super(updateHolder);
        this.textMessageSender = textMessageSender;
    }

    @Override
    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        textMessageSender.send(Long.valueOf(updateHolder.getChatId()), "Music added");
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
