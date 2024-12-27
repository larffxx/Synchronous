package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class RegisterCommand extends Command {
    private final UsersConnectDAO usersConnectDAO;

    public RegisterCommand(UpdateHolder updateHolder, UsersConnectDAO usersConnectDAO) {
        super(updateHolder);
        this.usersConnectDAO = usersConnectDAO;
    }

    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        SendMessage sm;
        String[] discordName = updateHolder.getUpdate().getMessage().getText().split(" ");
        String telegramName = updateHolder.getUpdate().getMessage().getFrom().getUserName();
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateHolder.getUpdate().getMessage().getFrom().getUserName());

        if (usersConnectDAO.getByTelegramName(telegramName).getTelegramName() != null) {
            sm = SendMessage.builder().chatId(updateHolder.getChatId()).text("You have been registered before").build();

            updateHolder.getTelegramClient().execute(sm);
        } else {
            usersConnectDAO.saveData(usersConnect);

            sm = SendMessage.builder().chatId(updateHolder.getChatId()).text("registered").build();

            updateHolder.getTelegramClient().execute(sm);
        }
    }

    public String getCommand() {
        return "/register";
    }
}
