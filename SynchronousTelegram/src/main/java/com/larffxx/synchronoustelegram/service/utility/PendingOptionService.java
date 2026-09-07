package com.larffxx.synchronoustelegram.service.utility;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Tracks chats that still owe options for a button initiated command.
 */
@Service
public class PendingOptionService {
    /**
     * Pending commands keyed by chat identifier.
     */
    private final ConcurrentHashMap<Long, String> pending = new ConcurrentHashMap<>();

    /**
     * Marks a chat as waiting for options for the given command.
     *
     * @param chatId Telegram chat expecting to provide options
     * @param command command waiting for the options
     */
    public void expectOptions(Long chatId, String command) {
        pending.put(chatId, command);
    }

    /**
     * Removes and returns the pending command for a chat.
     *
     * @param chatId Telegram chat whose pending command is taken
     * @return pending command name or null if none is pending
     */
    public String take(Long chatId) {
        return pending.remove(chatId);
    }

    /**
     * Discards the pending command for a chat.
     *
     * @param chatId Telegram chat whose pending command is cancelled
     */
    public void cancel(Long chatId) {
        pending.remove(chatId);
    }
}
