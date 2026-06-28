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
    private String name;
    private String message;
    private File file;
    private List<File> files;
    private String messageType;

    public MessagePayload(Long chatId, String name, String message, File file, String type) {
        this.name = name;
        this.file = file;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }

    public MessagePayload(Long chatId, String guildId, String name, String message, String type) {
        this.guildId = guildId;
        this.name = name;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }


    public String toString() {
        return "Name: " + this.name + " Message: " + this.message;
    }
}

