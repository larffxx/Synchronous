package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectCommand extends Command {
    private final TextMessageSender textMessageSender;
    private final ServersConnectRepository serversConnectRepository;

    public ConnectCommand(UpdateHandler updateHandler, TextMessageSender textMessageSender, ServersConnectRepository serversConnectRepository) {
        super(updateHandler);
        this.textMessageSender = textMessageSender;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(UpdateHandler updateHandler) throws TelegramApiException {
        if (serversConnectRepository.existsByTelegramChannel(updateHandler.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectRepository.updateByTelegramChannel(updateHandler.getChatId(), updateHandler.getUpdate().getMessage().getChat().getTitle());

            textMessageSender.send(Long.valueOf(updateHandler.getChatId()), "connected");
        } else {
            textMessageSender.send(Long.valueOf(updateHandler.getChatId()), "connected before");
        }
    }

    @Override
    public void execute(DiscordPayload discordPayload) throws TelegramApiException {
        String telegramChannelName = discordPayload.getCommand().getOptions().get(0);

        if (serversConnectRepository.existsByTelegramChannel(telegramChannelName)) {
            serversConnectRepository.updateByTelegramChannel(getUpdateHandler().getChatId(), telegramChannelName);

            textMessageSender.send(Long.valueOf(getUpdateHandler().getChatId()), "connected");
        } else {
            textMessageSender.send(Long.valueOf(getUpdateHandler().getChatId()), "connected before");
        }
    }


    @Override
    public String getCommand() {
        return "/connect";
    }
}
