package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.exception.execution.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.domain.exception.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.record.PayloadContext;
import com.larffxx.synchronoustelegram.infrastructure.PayloadContextResolver;
import com.larffxx.synchronoustelegram.infrastructure.payload.DiscordPayload;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.handler.message.UserMentionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TextMessageSender implements Sender {
    private final UpdateReceiver updateReceiver;
    private final UserMentionHandler userMentionHandler;
    private final PayloadContextResolver payloadContextResolver;

    public TextMessageSender(UpdateReceiver updateReceiver, UserMentionHandler userMentionHandler, PayloadContextResolver payloadContextResolver) {
        this.updateReceiver = updateReceiver;
        this.userMentionHandler = userMentionHandler;
        this.payloadContextResolver = payloadContextResolver;
    }

    @Override
    public void send(DiscordPayload discordPayload) {
        PayloadContext context = payloadContextResolver.resolvePayloadContext(discordPayload);
        Long chatID = Long.valueOf(context.getChatID());

        String formattedMessage = userMentionHandler.convertMentionsToTelegramNames(discordPayload);

        SendMessage sm = SendMessage.builder().chatId(chatID).text(formattedMessage).build();

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
