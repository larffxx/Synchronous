package com.larffxx.synchronousdiscord.domain.constant.infmsg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SendersConstants {
    public static String MESSAGE_TYPE = "messageType";
    public static String MESSAGE_FROM_TELEGRAM = "message";
    public static String NAME_IN_TELEGRAM = "telegramUserName";
    public static String FILE_FROM_TELEGRAM = "file";
    public static String PHOTO_NAME = "photo.png";
    public static String PHOTO_ATTACHMENT = "attachment://photo.png";
}
