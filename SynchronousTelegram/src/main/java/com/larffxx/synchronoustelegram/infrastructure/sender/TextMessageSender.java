package com.larffxx.synchronoustelegram.infrastructure.sender;

import com.larffxx.synchronoustelegram.domain.exception.execution.SendingMessageToTelegramException;
import com.larffxx.synchronoustelegram.domain.constant.infexc.InfExcMessage;
import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.handler.message.UserMentionHandler;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Sends text messages to Telegram chats.
 * Converts Discord mentions to Telegram names before delivery and supports optional inline keyboards.
 */
@Component
public class TextMessageSender implements Sender {
    /**
     * Receiver that exposes the Telegram client used for sending.
     */
    private final UpdateReceiver updateReceiver;
    /**
     * Handler that rewrites Discord mentions into Telegram names.
     */
    private final UserMentionHandler userMentionHandler;

    /**
     * Creates the sender with its collaborators.
     * @param updateReceiver receiver exposing the Telegram client
     * @param userMentionHandler handler for mention conversion
     */
    public TextMessageSender(UpdateReceiver updateReceiver, UserMentionHandler userMentionHandler) {
        this.updateReceiver = updateReceiver;
        this.userMentionHandler = userMentionHandler;
    }

    /**
     * Sends a synced Discord message to its Telegram chat.
     * @param messageContext the synced message context
     * @throws SendingMessageToTelegramException if the Telegram API call fails
     */
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

    /**
     * Sends a plain text message to the given chat.
     * @param chatID the target chat identifier
     * @param message the text to send
     * @throws SendingMessageToTelegramException if the Telegram API call fails
     */
    public void send(Long chatID, String message) {
        SendMessage sm = SendMessage.builder().chatId(chatID).text(message).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }

    /**
     * Sends a text message with an inline keyboard to the given chat.
     * @param chatID the target chat identifier
     * @param message the text to send
     * @param keyboard the inline keyboard to attach
     * @throws SendingMessageToTelegramException if the Telegram API call fails
     */
    public void send(Long chatID, String message, InlineKeyboardMarkup keyboard) {
        SendMessage sm = SendMessage.builder().chatId(chatID).text(message).replyMarkup(keyboard).build();

        try {
            updateReceiver.getTelegramClient().execute(sm);
        } catch (TelegramApiException e) {
            throw new SendingMessageToTelegramException(InfExcMessage.SENDING_TEXT_TO_TELEGRAM_EXCEPTION);
        }
    }
}
