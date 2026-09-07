package com.larffxx.synchronoustelegram.service.message;

import com.larffxx.synchronoustelegram.domain.context.MessageContext;
import com.larffxx.synchronoustelegram.infrastructure.receiver.UpdateReceiver;
import com.larffxx.synchronoustelegram.infrastructure.sender.TextMessageSender;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

/**
 * Message service that sends text messages to Telegram chats.
 */
@Component
public class TextMessageService implements MessageService {
    /**
     * Receiver that provides the target chat for sending.
     */
    private final UpdateReceiver updateReceiver;
    /**
     * Sender that delivers text payloads to Telegram.
     */
    private final TextMessageSender textMessageSender;

    /**
     * Creates a text message service with its dependencies.
     *
     * @param updateReceiver receiver providing the target chat
     * @param textMessageSender sender delivering text payloads
     */
    public TextMessageService(UpdateReceiver updateReceiver, TextMessageSender textMessageSender) {
        this.updateReceiver = updateReceiver;
        this.textMessageSender = textMessageSender;
    }

    /**
     * Sends the text message described by the given context.
     *
     * @param messageContext classified message context to send
     */
    @Override
    public void send(MessageContext messageContext) {
        updateReceiver.setChatId(String.valueOf(messageContext.getTelegramChatId()));

        textMessageSender.send(messageContext);
    }

    /**
     * Sends plain text to the given chat.
     *
     * @param chatId target Telegram chat identifier
     * @param text message text to send
     */
    public void send(Long chatId, String text) {
        textMessageSender.send(chatId, text);
    }

    /**
     * Sends text with an inline keyboard to the given chat.
     *
     * @param chatId target Telegram chat identifier
     * @param text message text to send
     * @param keyboard inline keyboard attached to the message
     */
    public void send(Long chatId, String text, InlineKeyboardMarkup keyboard) {
        textMessageSender.send(chatId, text, keyboard);
    }

    /**
     * Returns the message type handled by this service.
     *
     * @return text message type identifier
     */
    @Override
    public String getType() {
        return "TEXT_MESSAGE";
    }
}
