package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectCommand extends Command {
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(UpdateHolder updateHolder, ServersConnectDAO serversConnectDAO) {
        super(updateHolder);
        this.serversConnectDAO = serversConnectDAO;
    }


    @Override
    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        SendMessage sm;
        if (serversConnectDAO.existsByTelegramChatName(updateHolder.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectDAO.updateTelegramChannel(updateHolder.getChatId(), updateHolder.getUpdate().getMessage().getChat().getTitle());

            sm = SendMessage.builder().chatId(updateHolder.getChatId()).text("connected").build();

            updateHolder.getTelegramClient().execute(sm);
        } else {
            sm = SendMessage.builder().chatId(updateHolder.getChatId()).text("connected before").build();

            updateHolder.getTelegramClient().execute(sm);
        }
    }


    @Override
    public String getCommand() {
        return "/connect";
    }
}
