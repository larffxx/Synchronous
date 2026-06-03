package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import org.springframework.stereotype.Service;

@Service
public class RegisterCommand extends Command {
    private final TextMessageService textMessageService;
    private final UsersConnectRepository usersConnectRepository;
    private final ServersConnectRepository serversConnectRepository;

    public RegisterCommand(UpdateReceiver updateReceiver, TextMessageService textMessageService, UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        super(updateReceiver);
        this.textMessageService = textMessageService;
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {
        Long chatID = updateReceiver.getUpdate().getMessage().getChat().getId();
        String[] discordName = updateReceiver.getUpdate().getMessage().getText().split(" ");
        String telegramName = updateReceiver.getUpdate().getMessage().getFrom().getUserName();
        UsersConnect usersConnect = new UsersConnect(discordName[1], updateReceiver.getUpdate().getMessage().getFrom().getUserName());

        if (usersConnectRepository.findByTelegramName(telegramName).getTelegramName() != null) {
            textMessageService.send(chatID, "You have been registered before");
        } else {
            usersConnectRepository.save(usersConnect);

            textMessageService.send(chatID, "registered");
        }
    }

    @Override
    public void execute(DiscordPayload discordPayload) {
        String discordName = discordPayload.getAuthor();
        String telegramName = discordPayload.getCommand().getOptions().get(0);
        UsersConnect usersConnect = new UsersConnect(discordName, telegramName);
        Long telegramChatID = Long.valueOf(serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel());

        if (usersConnectRepository.findByTelegramName(telegramName).getTelegramName() != null) {
            textMessageService.send(telegramChatID, "You have been registered before");
        } else {
            usersConnectRepository.save(usersConnect);

            textMessageService.send(telegramChatID, "registered");
        }
    }

    public String getCommand() {
        return "/register";
    }
}
