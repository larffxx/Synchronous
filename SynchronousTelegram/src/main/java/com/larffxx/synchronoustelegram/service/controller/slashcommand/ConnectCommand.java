package com.larffxx.synchronoustelegram.service.controller.slashcommand;

import com.larffxx.synchronoustelegram.domain.exception.command.TooManyOptionsException;
import com.larffxx.synchronoustelegram.domain.model.ServersConnect;
import com.larffxx.synchronoustelegram.domain.record.CommandContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.command.ConnectCommandException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.service.message.TextMessageService;
import com.larffxx.synchronoustelegram.service.controller.Command;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChat;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class ConnectCommand implements Command {
    private final UpdateReceiver updateReceiver;
    private final TextMessageService textMessageService;
    private final ServersConnectRepository serversConnectRepository;

    public ConnectCommand(UpdateReceiver updateReceiver, TextMessageService textMessageService, ServersConnectRepository serversConnectRepository) {
        this.updateReceiver = updateReceiver;
        this.textMessageService = textMessageService;
        this.serversConnectRepository = serversConnectRepository;
    }

    @Override
    public void execute(CommandContext commandContext) {
        if(commandContext.options().size() > 1){
            throw new TooManyOptionsException(InfExcMessage.TOO_MANY_OPTIONS_FOR_COMMAND_EXCEPTION);
        }
        Long chatID = commandContext.chatId();

        if (!serversConnectRepository.existsByTelegramChannel(String.valueOf(commandContext.chatId()))) {
            serversConnectRepository.save(new ServersConnect(commandContext.options().get(0), String.valueOf(chatID)));

            textMessageService.send(chatID, "connected");
        } else {
            textMessageService.send(chatID, "connected before");
        }
    }


    @Override
    public void execute(DiscordPayload discordPayload) {
        String telegramChatId = serversConnectRepository.findByDiscordGuild(String.valueOf(discordPayload.getGuildID())).getTelegramChannel();
        Chat chat;

        try {
            chat = updateReceiver.getTelegramClient().execute(new GetChat("@"+telegramChatId));
        } catch (TelegramApiException e) {
            throw new ConnectCommandException(InfExcMessage.TELEGRAM_CHANNEL_NAME_PARSING_EXCEPTION);
        }

        serversConnectRepository.updateByTelegramChannel(String.valueOf(chat.getId()), telegramChatId);
    }


    @Override
    public String getCommand() {
        return "connect";
    }
}
