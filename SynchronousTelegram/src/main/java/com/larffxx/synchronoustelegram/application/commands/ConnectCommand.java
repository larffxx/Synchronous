package com.larffxx.synchronoustelegram.application.commands;

import com.larffxx.synchronoustelegram.application.handler.UpdateHandler;
import com.larffxx.synchronoustelegram.domain.exception.command.CommandException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;
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
    public void execute(UpdateHandler updateHandler) {
        if (serversConnectRepository.existsByTelegramChannel(updateHandler.getUpdate().getMessage().getChat().getTitle())) {
            serversConnectRepository.updateByTelegramChannel(updateHandler.getChatId(), updateHandler.getUpdate().getMessage().getChat().getTitle());

            textMessageSender.send(Long.valueOf(updateHandler.getChatId()), "connected");
        } else {
            textMessageSender.send(Long.valueOf(updateHandler.getChatId()), "connected before");
        }
    }


    @Override
    public void execute(DiscordPayload discordPayload) {
        String telegramChannelName = discordPayload.getCommand().getOptions().get(0);
        Chat chat;

        try {
            chat = getUpdateHandler().getTelegramClient().execute(new GetChat("@"+telegramChannelName));
        } catch (TelegramApiException e) {
            throw new CommandException(InfExcMessage.TELEGRAM_CHANNEL_NAME_PARSING_EXCEPTION);
        }

        serversConnectRepository.updateByTelegramChannel(String.valueOf(chat.getId()), telegramChannelName);
    }


    @Override
    public String getCommand() {
        return "/connect";
    }
}
