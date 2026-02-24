package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;

@Component
public class RegisterCommand extends Command {
    private final TextMessageSender textMessageSender;
    private final UsersConnectRepository usersConnectRepository;

    public RegisterCommand(UpdateReceiver updateReceiver, TextMessageSender textMessageSender, UsersConnectRepository usersConnectRepository) {
        super(updateReceiver);
        this.textMessageSender = textMessageSender;
        this.usersConnectRepository = usersConnectRepository;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {
        String[] discordName = updateReceiver.getUpdate().getMessage().getText().split(" ");
        String telegramName = updateReceiver.getUpdate().getMessage().getFrom().getUserName();
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateReceiver.getUpdate().getMessage().getFrom().getUserName());

        if (usersConnectRepository.findByTelegramName(telegramName).getTelegramName() != null) {
            textMessageSender.send(Long.valueOf(updateReceiver.getChatId()), "You have been registered before");
        } else {
            usersConnectRepository.save(usersConnect);

            textMessageSender.send(Long.valueOf(updateReceiver.getChatId()), "registered");
        }
    }

    @Override
    public void execute(DiscordPayload discordPayload) {
        String discordName = discordPayload.getAuthor();
        String telegramName = discordPayload.getCommand().getOptions().get(0);
        UsersConnect usersConnect = new UsersConnect(discordName, telegramName);

        if (usersConnectRepository.findByTelegramName(telegramName).getTelegramName() != null) {
            textMessageSender.send(discordPayload.getGuildID(), "You have been registered before");
        } else {
            usersConnectRepository.save(usersConnect);

            textMessageSender.send(discordPayload.getGuildID(), "registered");
        }
    }

    public String getCommand() {
        return "/register";
    }
}
