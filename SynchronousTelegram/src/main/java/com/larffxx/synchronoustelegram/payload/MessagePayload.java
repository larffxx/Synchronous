package com.larffxx.synchronoustelegram.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class MessagePayload{
    private Long chatId;
    private String name;
    private String message;
    private File file;
    private String command;

    public MessagePayload(String name, String message, Long chatId) {
        this.name = name;
        this.message = message;
        this.chatId = chatId;
    }

    public MessagePayload(String name, String command, String message, Long chatId) {
        this.name = name;
        this.command = command;
        this.message = message;
        this.chatId = chatId;
    }

    public MessagePayload(String name, File file, Long chatId) {
        this.name = name;
        this.file = file;
        this.chatId = chatId;
    }

    public MessagePayload(String name, String message, File file, Long chatId) {
        this.name = name;
        this.file = file;
        this.message = message;
        this.chatId = chatId;
    }

    public String toString() {
        return "Name: " + this.name + " Message: " + this.message;
    }
}

