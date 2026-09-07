package com.larffxx.synchronousdiscord.domain.constant.infmsg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Holds sender name constants.
 */
@Component
@Getter
@Setter
public class SendersConstants {
    /**
     * The message type.
     */
    public static String MESSAGE_TYPE = "messageType";
    /**
     * The message from telegram.
     */
    public static String MESSAGE_FROM_TELEGRAM = "message";
    /**
     * The name in telegram.
     */
    public static String NAME_IN_TELEGRAM = "telegramUserName";
    /**
     * The file from telegram.
     */
    public static String FILE_FROM_TELEGRAM = "file";
    /**
     * The photo name.
     */
    public static String PHOTO_NAME = "photo.png";
    /**
     * The photo attachment.
     */
    public static String PHOTO_ATTACHMENT = "attachment://photo.png";
}
