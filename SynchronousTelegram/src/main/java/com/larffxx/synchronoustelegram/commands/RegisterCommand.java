package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class RegisterCommand implements Command<SendMessage> {
    private final UsersConnectDAO usersConnectDAO;

    public RegisterCommand(UsersConnectDAO usersConnectDAO) {
        this.usersConnectDAO = usersConnectDAO;
    }

    public SendMessage execute(UpdateReceiver updateReceiver) {
        SendMessage sm = null;
        String[] discordName = updateReceiver.getUpdate().getMessage().getText().split(" ");
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateReceiver.getUpdate().getMessage().getFrom().getUserName());

        try {
            if (this.usersConnectDAO.getByTelegramName(updateReceiver.getUpdate().getMessage().getFrom().getUserName()).equals(usersConnect.getTelegramName())) {
                sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("You have been registered before").build();
            }
        } catch (NullPointerException e) {
            usersConnectDAO.saveData(usersConnect);
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("registered").build();
        }

        return sm;
    }

    public String getCommand() {
        return "/register";
    }
}
