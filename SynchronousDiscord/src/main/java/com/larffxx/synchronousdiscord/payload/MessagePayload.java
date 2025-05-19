package com.larffxx.synchronousdiscord.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.net.URI;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MessagePayload {
    private Long guildId;
    private String authorName;
    private String message;
    private List<File> files;
    private String command;

    public MessagePayload(String authorName, String message, Long chatId){
        this.authorName = authorName;
        this.message = message;
        this.guildId = chatId;
    }
    public MessagePayload(Long guildId, String authorName, String message){
        this.guildId = guildId;
        this.authorName = authorName;
        this.message = message;
    }
    public MessagePayload(Long guildId, String authorName, List<File> files){
        this.guildId = guildId;
        this.authorName = authorName;
        this.files = files;
    }
}
