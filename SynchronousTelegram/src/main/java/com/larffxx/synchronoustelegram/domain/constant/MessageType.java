package com.larffxx.synchronoustelegram.domain.constant;

/**
 * Types of messages processed by the Telegram module.
 * Distinguishes photo and text messages from commands and button callbacks.
 */
public enum MessageType {
    /**
     * Message type constants: photo message, text message, slash command, button callback query.
     */
    PHOTO_MESSAGE, TEXT_MESSAGE, COMMAND, CALLBACK_QUERY
}
