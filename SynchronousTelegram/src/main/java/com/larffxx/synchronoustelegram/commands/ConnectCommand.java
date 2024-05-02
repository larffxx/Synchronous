package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.ServersConnectDAO;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class ConnectCommand implements Command<SendMessage> {
    private final ServersConnectDAO serversConnectDAO;

    public ConnectCommand(ServersConnectDAO serversConnectDAO) {
        this.serversConnectDAO = serversConnectDAO;
    }

    @Override
    public SendMessage execute(UpdateReceiver updateReceiver) {
        SendMessage sm;
        System.out.println(updateReceiver.getUpdate().getMessage().getChat().getTitle());
        if(serversConnectDAO.existsByTelegramChatName(updateReceiver.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectDAO.updateTelegramChannel(updateReceiver.getChatId(), updateReceiver.getUpdate().getMessage().getChat().getTitle());
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("connected").build();
        }else {
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("connected before").build();
        }
        return sm;
    }

    @Override
    public String getCommand() {
        return "connect";
    }
}
