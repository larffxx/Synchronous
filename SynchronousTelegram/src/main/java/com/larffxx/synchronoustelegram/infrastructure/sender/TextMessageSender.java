package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.domain.exception.execution.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.repo.ServersConnectRepository;
import com.larffxx.synchronoustelegram.infrastructure.sender.utility.UserMentionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class TextMessageSender {
    private final UpdateReceiver updateReceiver;
    private final ServersConnectRepository serversConnectRepository;
    private final UserMentionHandler userMentionHandler;

    public TextMessageSender(UpdateReceiver updateReceiver, ServersConnectRepository serversConnectRepository, UserMentionHandler userMentionHandler) {
        this.updateReceiver = updateReceiver;
        this.serversConnectRepository = serversConnectRepository;
        this.userMentionHandler = userMentionHandler;
    }

    public void send(Long id, String text) {
        String telegramChatId = serversConnectRepository.findByTelegramChannel(String.valueOf(id)).getTelegramChannel();
        updateReceiver.setChatId(telegramChatId);

        SendMessage sm = SendMessage.builder().chatId(telegramChatId).text(text).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }

    public void send(DiscordPayload payload) {
        updateReceiver.setChatId(serversConnectRepository.findByDiscordGuild(String.valueOf(payload.getGuildID())).getTelegramChannel());
        Long chatID = Long.valueOf(updateReceiver.getChatId());

        String formattedMessage = userMentionHandler.convertMentionsToTelegramNames(payload);

        SendMessage sm = SendMessage.builder().chatId(chatID).text(formattedMessage).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_FROM_DISCORD_EXCEPTION);
        }
    }
}