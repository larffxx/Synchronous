package com.larffxx.synchronoustelegram.infrastructure.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessagePayload{
    private Long chatId;
    private String guildId;
    private String telegramUserName;
    private String message;
    private File file;
    private List<File> files;
    private String messageType;

    public MessagePayload(Long chatId, String guildId, String telegramUserName, String message, String type) {
        this.guildId = guildId;
        this.telegramUserName = telegramUserName;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }

    public MessagePayload(Long chatId, String guildId, String telegramUserName, String message, File photoFile, String messageType) {
        this.chatId = chatId;
        this.guildId = guildId;
        this.telegramUserName = telegramUserName;
        this.message = message;
        this.file = photoFile;
        this.messageType = messageType;
    }


    public String toString() {
        return "Name: " + this.telegramUserName + " Message: " + this.message;
    }
}

