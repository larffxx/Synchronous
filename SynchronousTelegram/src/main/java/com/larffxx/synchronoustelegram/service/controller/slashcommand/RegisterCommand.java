package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.domain.model.UsersConnect;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.repo.UsersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import org.springframework.stereotype.Service;

@Service
public class RegisterCommand implements Command {
    private final TextMessageService textMessageService;
    private final UsersConnectRepository usersConnectRepository;
    private final ServersConnectRepository serversConnectRepository;

    public RegisterCommand(TextMessageService textMessageService, UsersConnectRepository usersConnectRepository, ServersConnectRepository serversConnectRepository) {
        this.textMessageService = textMessageService;
        this.usersConnectRepository = usersConnectRepository;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(CommandContext commandContext) {
        if(commandContext.options().isEmpty() || commandContext.options().size() > 2) {
            //TODO: exception
            throw new RuntimeException();
        }

        Long chatID = commandContext.chatId();
        String discordName = commandContext.options().get(0);
        String telegramName = commandContext.commandAuthorName();
        UsersConnect usersConnect = new UsersConnect(discordName, telegramName);

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
        return "register";
    }
}
