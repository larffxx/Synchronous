package com.larffxx.synchronoustelegram.commands;

import com.larffxx.synchronoustelegram.dao.UsersConnectDAO;
import com.larffxx.synchronoustelegram.models.UsersConnect;
import com.larffxx.synchronoustelegram.holder.UpdateHolder;
import com.larffxx.synchronoustelegram.sender.TextMessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class RegisterCommand extends Command {
    private final TextMessageSender textMessageSender;
    private final UsersConnectDAO usersConnectDAO;

    public RegisterCommand(UpdateHolder updateHolder, TextMessageSender textMessageSender, UsersConnectDAO usersConnectDAO) {
        super(updateHolder);
        this.textMessageSender = textMessageSender;
        this.usersConnectDAO = usersConnectDAO;
    }

    public void execute(UpdateHolder updateHolder) throws TelegramApiException {
        String[] discordName = updateHolder.getUpdate().getMessage().getText().split(" ");
        String telegramName = updateHolder.getUpdate().getMessage().getFrom().getUserName();
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateHolder.getUpdate().getMessage().getFrom().getUserName());

        if (usersConnectDAO.getByTelegramName(telegramName).getTelegramName() != null) {
            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), "You have been registered before");
        } else {
            usersConnectDAO.saveData(usersConnect);

            textMessageSender.send(Long.valueOf(updateHolder.getChatId()), "registered");
        }
    }

    public String getCommand() {
        return "/register";
    }
}
