package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectCommand implements Command {
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(ServersConnectDAO serversConnectDAO) {
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {
        SendMessage sm;
        System.out.println(updateReceiver.getUpdate().getMessage().getChat().getTitle());
        if(serversConnectDAO.existsByTelegramChatName(updateReceiver.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectDAO.updateTelegramChannel(updateReceiver.getChatId(), updateReceiver.getUpdate().getMessage().getChat().getTitle());
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("connected").build();
            try {
                updateReceiver.getTelegramClient().execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else {
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("connected before").build();
            try {
                updateReceiver.getTelegramClient().execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getCommand() {
        return "connect";
    }
}
