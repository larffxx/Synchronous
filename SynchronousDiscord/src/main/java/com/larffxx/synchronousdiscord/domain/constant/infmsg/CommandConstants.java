package com.larffxx.synchronousdiscord.domain.constant.infmsg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Holds slash command name constants.
 */
@Component
@Getter
@Setter
public class CommandConstants {
    /**
     * TELEGRAM CHAT ID constant.
     */
    public static final String TELEGRAM_CHAT_ID = "chatId";
    /**
     * DISCORD TEXT CHANNEL constant.
     */
    public static final String DISCORD_TEXT_CHANNEL = "telegram";
    /**
     * COMMAND VALUE constant.
     */
    public static final String COMMAND_VALUE = "command";
    /**
     * COMMAND OPTIONS constant.
     */
    public static final String COMMAND_OPTIONS = "options";
    /**
     * TELEGRAM CHANNEL NAME FROM OPTIONS constant.
     */
    public static final String TELEGRAM_CHANNEL_NAME_FROM_OPTIONS = "telegram";
    /**
     * CONNECT SUCCESS MESSAGE constant.
     */
    public static final String CONNECT_SUCCESS_MESSAGE = "Servers connected successfully!";
    /**
     * NAME FROM TELEGRAM constant.
     */
    public static final String NAME_FROM_TELEGRAM = "name";
    /**
     * USER REGISTER SUCCESS MESSAGE constant.
     */
    public static final String USER_REGISTER_SUCCESS_MESSAGE = "Successfully registered";
    /**
     * USER REGISTER UNSUCCESSFUL MESSAGE constant.
     */
    public static final String USER_REGISTER_UNSUCCESSFUL_MESSAGE = "You have been registered before";
    /**
     * DELETE SUCCESS MESSAGE constant.
     */
    public static final String DELETE_SUCCESS_MESSAGE = "Successfully deleted %s messages";
}
