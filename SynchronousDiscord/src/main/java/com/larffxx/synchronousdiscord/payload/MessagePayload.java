package com.larffxx.synchronousdiscord.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
public class MessagePayload {
    private Long guildId;
    private String name;
    private String message;
    private File file;
    private String command;

    public MessagePayload(String name, String message, Long chatId){
        this.name = name;
        this.message = message;
        this.guildId = chatId;
    }
}
