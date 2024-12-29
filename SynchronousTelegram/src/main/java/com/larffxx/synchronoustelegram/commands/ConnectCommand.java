package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.sender.TextMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectCommand extends Command {
    private final TextMessageSender textMessageSender;
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(UpdateHolder updateHolder, TextMessageSender textMessageSender, ServersConnectDAO serversConnectDAO) {
        super(updateHolder);
        this.textMessageSender = textMessageSender;
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        if (serversConnectDAO.existsByTelegramChatName(updateHolder.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectDAO.updateTelegramChannel(updateHolder.getChatId(), updateHolder.getUpdate().getMessage().getChat().getTitle());

            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), "connected");
        } else {
            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), "connected before");
        }
    }


    @Override
    public String getCommand() {
        return "/connect";
    }
}
