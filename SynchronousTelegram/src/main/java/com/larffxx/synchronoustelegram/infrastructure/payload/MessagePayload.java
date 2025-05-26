package com.larffxx.synchronoustelegram.infrastructure.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class MessagePayload{
    private Long chatId;
    private String name;
    private String message;
    private File file;
    private List<File> files;
    private String messageType;

    public MessagePayload(String name, String message, Long chatId, String type) {
        this.name = name;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }

    public MessagePayload(Long chatId, String name, List<File> file, String type) {
        this.name = name;
        this.files = file;
        this.chatId = chatId;
        this.messageType = type;
    }

    public MessagePayload(Long chatId, String name, String message, List<File> file, String type) {
        this.name = name;
        this.files = file;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }

    public MessagePayload(Long chatId, String name, String message, File file, String type) {
        this.name = name;
        this.file = file;
        this.message = message;
        this.chatId = chatId;
        this.messageType = type;
    }
    public MessagePayload(Long chatId, String name,  File file, String type) {
        this.name = name;
        this.file = file;
        this.chatId = chatId;
        this.messageType = type;
    }


    public String toString() {
        return "Name: " + this.name + " Message: " + this.message;
    }
}

