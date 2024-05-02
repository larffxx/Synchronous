package com.larffxx.synchronousdiscord.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class MessagePayload {
    private Long chatId;
    private String name;
    private String message;
    private File file;
    private String command;

    public MessagePayload(String name, String message, Long chatId){
        this.name = name;
        this.message = message;
        this.chatId = chatId;
    }


    @Override
    public String toString() {
        return "Name: " + name + " " + "Message: " + message;
    }
}
