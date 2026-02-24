package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class PlayCommand extends Command{
    private final ServersConnectRepository serversConnectRepository;
    private final TextMessageSender textMessageSender;

    public PlayCommand(UpdateReceiver updateReceiver, ServersConnectRepository serversConnectRepository, TextMessageSender textMessageSender) {
        super(updateReceiver);
        this.serversConnectRepository = serversConnectRepository;
        this.textMessageSender = textMessageSender;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver){
        textMessageSender.send(Long.valueOf(updateReceiver.getChatId()), "Music added");
    }

    @Override
    public void execute(DiscordPayload discordPayload) {
        Long telegramChatId = Long.valueOf(serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel());

        textMessageSender.send(telegramChatId, "Music added");
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
