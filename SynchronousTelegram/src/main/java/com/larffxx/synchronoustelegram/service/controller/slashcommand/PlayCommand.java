package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PlayCommand extends Command {
    private final ServersConnectRepository serversConnectRepository;
    private final TextMessageService textMessageService;

    public PlayCommand(UpdateReceiver updateReceiver, ServersConnectRepository serversConnectRepository, TextMessageService textMessageService) {
        super(updateReceiver);
        this.serversConnectRepository = serversConnectRepository;
        this.textMessageService = textMessageService;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver){
        textMessageService.send(Long.valueOf(updateReceiver.getChatId()), "Music added");
    }

    @Override
    public void execute(DiscordPayload discordPayload) {
        Long telegramChatId = Long.valueOf(serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel());

        textMessageService.send(telegramChatId, "Music added");
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
