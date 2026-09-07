package com.larffxx.synchronoustelegram.domain.constant.infexc;

/**
 * Central store of exception message templates.
 * Holds the user facing texts used when domain exceptions are created.
 */
public class InfExcMessage {
    /**
     * Message for failures while executing a raw string command.
     */
    public static String WHILE_EXECUTE_STRING_COMMAND_EXCEPTION = "String command execution failed";
    /**
     * Message template for failures while receiving a photo from a media group.
     */
    public static String RECEIVING_PHOTO_FROM_MEDIA_GROUP_EXCEPTION = "Receiving photo exception %s";
    /**
     * Message for failures while sending a photo to Telegram.
     */
    public static String SENDING_PHOTO_EXCEPTION = "Sending photo exception";
    /**
     * Message for failures while sending text to Telegram.
     */
    public static String SENDING_TEXT_TO_TELEGRAM_EXCEPTION = "Sending text to telegram exception";
    /**
     * Message for failures while sending text received from Discord.
     */
    public static String SENDING_TEXT_FROM_DISCORD_EXCEPTION = "Sending text from discord exception";
    /**
     * Message for failures while downloading an image.
     */
    public static String DOWNLOAD_IMAGE_EXCEPTION = "Download image exception";
    /**
     * Message template for failures while converting a photo.
     */
    public static String CONVERSION_PHOTO_EXCEPTION = "Conversion photo exception: %s";
    /**
     * Message for commands with an unexpected format.
     */
    public static String INVALID_COMMAND_EXCEPTION = "Invalid command";
    /**
     * Message for commands that received more options than allowed.
     */
    public static String TOO_MANY_OPTIONS_FOR_COMMAND_EXCEPTION = "Too many options for command";
    /**
     * Message for commands that received no options.
     */
    public static String NO_OPTIONS_FOR_COMMAND_EXCEPTION = "No options for command";
    /**
     * Message for generic Telegram failures.
     */
    public static String TELEGRAM_EXCEPTION = "Telegram exception";
    /**
     * Message for generic command failures.
     */
    public static String COMMAND_EXCEPTION = "Command exception";
    /**
     * Message for generic data failures.
     */
    public static String DATA_EXCEPTION = "Data exception";
    /**
     * Message for generic execution failures.
     */
    public static String EXECUTION_EXCEPTION = "Execution exception";
}
