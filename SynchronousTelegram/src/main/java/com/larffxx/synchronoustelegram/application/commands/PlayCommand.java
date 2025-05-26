package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Getter
@Setter
public class PlayCommand extends Command{
    private final ServersConnectRepository serversConnectRepository;
    private final TextMessageSender textMessageSender;

    public PlayCommand(UpdateHandler updateHandler, ServersConnectRepository serversConnectRepository, TextMessageSender textMessageSender) {
        super(updateHandler);
        this.serversConnectRepository = serversConnectRepository;
        this.textMessageSender = textMessageSender;
    }

    @Override
    public void execute(UpdateHandler updateHandler) throws TelegramApiException {
        textMessageSender.send(Long.valueOf(updateHandler.getChatId()), "Music added");
    }

    @Override
    public void execute(DiscordPayload discordPayload) throws TelegramApiException {
        Long telegramChatId = Long.valueOf(serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel());

        textMessageSender.send(telegramChatId, "Music added");
    }

    @Override
    public String getCommand() {
        return "/play";
    }
}
