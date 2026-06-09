package com.larffxx.synchronoustelegram.infrastructure.payload;

import com.larffxx.synchronoustelegram.domain.constant.MessageType;
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
public class DiscordPayload {
    private Long guildID;
    private String author;
    private String message;
    private CommandPayload commandPayload;
    private List<File> files;
    private MessageType messageType;

    public DiscordPayload(Long guildID, String author, String message, List<File> files) {
        this.guildID = guildID;
        this.author = author;
        this.message = message;
        this.files = files;
        this.messageType = MessageType.PHOTO_MESSAGE;
    }

    public DiscordPayload(Long guildID, String author, CommandPayload command) {
        this.guildID = guildID;
        this.author = author;
        this.commandPayload = command;
    }

    public DiscordPayload(Long guildID, String author, String message, List<File> files, MessageType messageType) {
        this.guildID = guildID;
        this.author = author;
        this.message = message;
        this.files = files;
        this.messageType = messageType;
    }
}
