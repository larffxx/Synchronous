package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.command.ConnectCommandException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class ConnectCommand extends Command {
    private final TextMessageService textMessageService;
    private final ServersConnectRepository serversConnectRepository;

    public ConnectCommand(UpdateReceiver updateReceiver, TextMessageService textMessageService, ServersConnectRepository serversConnectRepository) {
        super(updateReceiver);
        this.textMessageService = textMessageService;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(UpdateReceiver updateReceiver) {
        if (!serversConnectRepository.existsByTelegramChannel(String.valueOf(updateReceiver.getUpdate().getMessage().getChat().getId()))) {
            serversConnectRepository.save(new ServersConnect(updateReceiver.getOption(), updateReceiver.getChatId()));

            textMessageService.send(Long.valueOf(updateReceiver.getChatId()), "connected");
        } else {
            textMessageService.send(Long.valueOf(updateReceiver.getChatId()), "connected before");
        }
    }


    @Override
    public void execute(DiscordPayload discordPayload) {
        String telegramChannelName = discordPayload.getCommand().getOptions().get(0);
        Chat chat;

        try {
            chat = getUpdateReceiver().getTelegramClient().execute(new GetChat("@"+telegramChannelName));
        } catch (TelegramApiException e) {
            throw new ConnectCommandException(InfExcMessage.TELEGRAM_CHANNEL_NAME_PARSING_EXCEPTION);
        }

        serversConnectRepository.updateByTelegramChannel(String.valueOf(chat.getId()), telegramChannelName);
    }


    @Override
    public String getCommand() {
        return "/connect";
    }
}
