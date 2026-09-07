package com.larffxx.synchronoustelegram.domain.constant.infmsg;

/**
 * JSON field names shared by Kafka payload mappers.
 * Keeps producer and consumer field names consistent.
 */
public class MapperConstant {
    /**
     * JSON field name for the Discord guild identifier.
     */
    public final static String GUILD_ID = "guildId";
    /**
     * JSON field name for the Telegram chat identifier.
     */
    public final static String TELEGRAM_CHAT_ID = "telegramChatId";
    /**
     * JSON field name for the Discord user name.
     */
    public final static String DISCORD_USER_NAME = "discordUserName";
    /**
     * JSON field name for the message text.
     */
    public final static String MESSAGE = "message";
    /**
     * JSON field name for the message type.
     */
    public final static String MESSAGE_TYPE = "messageType";
    /**
     * JSON field name for the command name.
     */
    public final static String COMMAND_NAME = "commandName";
    /**
     * JSON field name for the command options.
     */
    public final static String OPTIONS = "options";
    /**
     * JSON field name for the command responses.
     */
    public final static String RESPONSE = "response";
    /**
     * JSON field name for the attached files.
     */
    public final static String FILES = "files";
}
