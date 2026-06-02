package com.larffxx.synchronousdiscord.infrastructure.payload;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.List;

@Getter
@Setter
public class MessagePayload {
    private Long guildId;
    private String authorName;
    private String message;
    private List<File> files;
    private MessageType messageType;

    public  MessagePayload(Long guildId, String authorName, String message, List<File> files) {
        this.guildId = guildId;
        this.authorName = authorName;
        this.message = message;
        this.files = files;
        this.messageType = MessageType.PHOTO_MESSAGE;
    }

    public  MessagePayload(Long guildId, String authorName, String message, List<File> files, MessageType messageType) {
        this.guildId = guildId;
        this.authorName = authorName;
        this.message = message;
        this.files = files;
        this.messageType = messageType;
    }
}
