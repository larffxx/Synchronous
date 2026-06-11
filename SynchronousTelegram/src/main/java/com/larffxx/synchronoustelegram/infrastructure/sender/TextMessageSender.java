package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.exception.execution.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.handler.message.UserMentionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TextMessageSender implements Sender {
    private final UpdateReceiver updateReceiver;
    private final UserMentionHandler userMentionHandler;

    public TextMessageSender(UpdateReceiver updateReceiver, UserMentionHandler userMentionHandler) {
        this.updateReceiver = updateReceiver;
        this.userMentionHandler = userMentionHandler;
    }

    @Override
    public void send(MessageContext messageContext) {
        String formattedMessage = userMentionHandler.convertMentionsToTelegramNames(messageContext);

        SendMessage sm = SendMessage.builder().chatId(messageContext.getTelegramChatId()).text(formattedMessage).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_FROM_DISCORD_EXCEPTION);
        }
    }

    public void send(Long chatID, String message) {
        SendMessage sm = SendMessage.builder().chatId(chatID).text(message).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }
}
