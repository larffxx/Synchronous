package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class PlayCommand implements Command {
    private final ServersConnectRepository serversConnectRepository;
    private final TextMessageService textMessageService;

    public PlayCommand(ServersConnectRepository serversConnectRepository, TextMessageService textMessageService) {
        this.serversConnectRepository = serversConnectRepository;
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(CommandContext commandContext){
        textMessageService.send(commandContext.chatId(), "Music added");
    }

    @Override
    public void execute(DiscordPayload discordPayload) {
        Long telegramChatId = Long.valueOf(serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel());

        textMessageService.send(telegramChatId, "Music added");
    }

    @Override
    public String getCommand() {
        return "play";
    }
}
