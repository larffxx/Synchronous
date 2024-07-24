package com.larffxx.synchronousdiscord.infmsg;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class SendersInfMessages {
    public static String MESSAGE_FROM_TELEGRAM = "message";
    public static String TELEGRAM_CHAT_ID = "chatId";
    public static String TEXT_CHANNEL_IN_DISCORD = "telegram";
    public static String NAME_IN_TELEGRAM = "name";
    public static String FILE_FROM_TELEGRAM = "file";
    public static String PHOTO_NAME = "photo.png";
    public static String PHOTO_ATTACHMENT = "attachment://photo.png";
}
