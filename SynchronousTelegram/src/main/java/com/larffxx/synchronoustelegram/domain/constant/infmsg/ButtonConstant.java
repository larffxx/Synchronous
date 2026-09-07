package com.larffxx.synchronoustelegram.domain.constant.infmsg;

/**
 * Constants and helpers for Telegram inline buttons.
 * Defines the callback data prefix and the prompts used when a button needs options.
 */
public class ButtonConstant {
    /**
     * Prefix that marks callback data as a button press.
     */
    public static final String CALLBACK_PREFIX = "button:";

    /**
     * Builds callback data for the given button.
     * @param button the button name
     * @return callback data with the button prefix
     */
    public static String callback(String button) {
        return CALLBACK_PREFIX + button;
    }

    /**
     * Returns the option prompt shown for the given button.
     * @param button the button name
     * @return the prompt text asking for the missing option
     */
    public static String optionPrompt(String button) {
        return switch (button) {
            case "connect" -> "Send telegram chat name:";
            case "register" -> "Send discord username:";
            default -> "Send option:";
        };
    }
}
