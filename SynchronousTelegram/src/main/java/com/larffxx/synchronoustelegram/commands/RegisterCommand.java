package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.receivers.UpdateReceiver;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class RegisterCommand implements Command {
    private final UsersConnectDAO usersConnectDAO;

    public RegisterCommand(UsersConnectDAO usersConnectDAO) {
        this.usersConnectDAO = usersConnectDAO;
    }

    public void execute(UpdateReceiver updateReceiver) {
        SendMessage sm;
        String[] discordName = updateReceiver.getUpdate().getMessage().getText().split(" ");
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateReceiver.getUpdate().getMessage().getFrom().getUserName());
        try {
            if (this.usersConnectDAO.getByTelegramName(updateReceiver.getUpdate().getMessage().getFrom().getUserName()).equals(usersConnect.getTelegramName())) {
                sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("You have been registered before").build();
                updateReceiver.getTelegramClient().execute(sm);
            }
        } catch (NullPointerException e) {
            usersConnectDAO.saveData(usersConnect);
            sm = SendMessage.builder().chatId(updateReceiver.getChatId()).text("registered").build();
            try {
                updateReceiver.getTelegramClient().execute(sm);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public String getCommand() {
        return "/register";
    }
}
